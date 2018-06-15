package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.transport.app.constants.StatementStatusEnum;
import me.rubick.transport.app.constants.StatementTypeEnum;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.repository.PaymentRepository;
import me.rubick.transport.app.repository.StatementsRepository;
import me.rubick.transport.app.repository.UserRepository;
import me.rubick.transport.app.vo.CostSubjectSnapshotVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.*;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class PayService {

    @Resource
    private PaymentRepository paymentRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private ConfigService configService;

    @Resource
    private PackageRepository packageRepository;

    @Resource
    private StockService stockService;


    public Payment findPayment(long paymentId, User user) {
        return paymentRepository.findTopByIdAndUserIdAndStatus(paymentId, user.getId(), PaymentStatus.FALSE);
    }

    public Payment createPaymentForAccount(User user, BigDecimal totalFee) throws BusinessException {
        totalFee = totalFee.setScale(2, RoundingMode.FLOOR);
        if (totalFee.compareTo(new BigDecimal("0.01")) < 0) {
            throw new BusinessException("支付金额必须大于0.01元");
        }

        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("[A001] 禁止访问！");
        }

        Payment payment = new Payment();
        payment.setUserId(user.getId());
        payment.setExtendsInfo("");
        payment.setOutTradeNo(generateOutTradeNo());
        payment.setStatus(PaymentStatus.FALSE);
        payment.setTotalFee(totalFee);
        payment.setType(PaymentType.ACCOUNT);

        return paymentRepository.save(payment);
    }

    private String generateOutTradeNo() {
        return UUID.randomUUID().toString();
    }

    /**
     * 支付成功的回调
     *
     * @param outTradeNo
     */
    public Payment successForPayment(String outTradeNo) throws BusinessException {
        Payment payment = paymentRepository.findTopByOutTradeNo(outTradeNo);

        if (ObjectUtils.isEmpty(payment)) {
            throw new BusinessException("");
        }

        //支付成功
        payment.setStatus(PaymentStatus.TRUE);
        payment.setSuccessAt(new Date());
        paymentRepository.save(payment);

        switch (payment.getType()) {
            case ACCOUNT:
                successForAccount(payment);
                break;
            case ORDER:
                break;
            default:
                ;
        }

        return payment;
    }

    private void successForAccount(Payment payment) {
        User user = userRepository.findOne(payment.getUserId());
        BigDecimal bigDecimal = payment.getTotalFee().divide(new BigDecimal(configService.findOneByKey("U2R")), 2, RoundingMode.FLOOR);
        user.setUsd(user.getUsd().add(bigDecimal));
        userRepository.save(user);
    }


    ////////////////////  收费

    @Resource
    private StatementsRepository statementsRepository;

    @Resource
    private UserService userService;

    public Page<Statements> findAllStatements(long userId, Pageable pageable) {
        return statementsRepository.findByUserId(userId, pageable);
    }


    public Statements saveStatements(Statements statements, BigDecimal total) {
        log.info("系统自动生成 = {}, 提交={}", statements.getTotal(), total);
        if (! ObjectUtils.isEmpty(total)) {
            statements.setTotal(total);
        }
        return statementsRepository.save(statements);
    }

    /**
     * 计算费用
     * @param p
     * @return
     */
    public Statements calcCK(Package p) {
        CostSubjectSnapshotVo costSubjectSnapshotVo = userService.findCostSubjectByUserId(p.getUserId());

        Statements statements = new Statements();
        statements.setUserId(p.getUserId());
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.RK);
        statements.setTarget(String.valueOf(p.getId()));
        statements.setPayAt(null);

        BigDecimal total = BigDecimal.ZERO;


        //总重、总体积
        BigDecimal tWeight = BigDecimal.ZERO;
//        BigDecimal tSize = BigDecimal.ZERO;

        for (PackageProduct pp : p.getPackageProducts()) {
            Product product = pp.getProduct();

            tWeight = tWeight.add(product.getWeight().multiply(new BigDecimal(pp.getQuantity())));
//            tSize = tSize.add(product.getVol().multiply(new BigDecimal(pp.getQuantity())));
        }

        String comment = MessageFormat.format("入库单：{0} ，总重：{1} KG", p.getReferenceNumber(), tWeight);
//        String comment = MessageFormat.format("入库单总重：{0} KG, 入库总体积：{1} 立方米", tWeight, tSize);

        switch (costSubjectSnapshotVo.getRkt()) {
            case "RK-AZ":       //按重
                total = tWeight.multiply(costSubjectSnapshotVo.getRkv());
                break;
            case "RK-AX":       //按箱
                total = costSubjectSnapshotVo.getRkv();
                break;
            case "RK-AF":       //免费
                break;
        }

        log.info("总价：{}", total);
        statements.setComment(comment);
        statements.setTotal(total);
        return statements;
    }

    public boolean payStatements(long id) {
        Statements statements = statementsRepository.findOne(id);

        if (statements.getStatus().equals(StatementStatusEnum.UNPAY)) {
            if (userService.payUSD(statements.getUserId(), statements.getTotal())) {
                //支付成功
                statements.setStatus(StatementStatusEnum.PAY);
                statements.setPayAt(new Date());
                statementsRepository.save(statements);
                return true;
            } else {
                log.info("余额不足");
                return false;
            }
        }

        return true;
    }

    public void secPayStatements(long id) throws BusinessException {
        Statements statements = statementsRepository.findOne(id);

        if (statements.getStatus().equals(StatementStatusEnum.UNPAY)) {
            if (userService.payUSD(statements.getUserId(), statements.getTotal())) {
                //支付成功
                statements.setStatus(StatementStatusEnum.PAY);
                statements.setPayAt(new Date());
                statementsRepository.save(statements);

                switch (statements.getType()) {
                    case RK: {
                        Package p = packageRepository.findOne(Long.valueOf(statements.getTarget()));
                        if (p.getNextStatus() != PackageStatus.NULL) {
                            p.setStatus(p.getNextStatus());
                            p.setNextStatus(PackageStatus.NULL);
                            packageRepository.save(p);
                        }
                        break;
                    }
                    case SJ: {
                        Package p = packageRepository.findOne(Long.valueOf(statements.getTarget()));
                        if (p.getNextStatus() != PackageStatus.NULL) {
                            p.setStatus(p.getNextStatus());
                            p.setNextStatus(PackageStatus.NULL);
                            packageRepository.save(p);

                            stockService.addStock(p);
                        }
                        break;
                    }
                }
            } else {
                throw new BusinessException("余额不足");
            }
        }
    }

    /**
     * 上架费
     * @param p
     * @return
     */
    public Statements calcSJ(Package p) {
        CostSubjectSnapshotVo costSubjectSnapshotVo = userService.findCostSubjectByUserId(p.getUserId());

        Statements statements = new Statements();
        statements.setUserId(p.getUserId());
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.SJ);
        statements.setTarget(String.valueOf(p.getId()));
        statements.setPayAt(null);

        BigDecimal total = BigDecimal.ZERO;


        //总重、总体积
        int count = 0;
        BigDecimal tSize = BigDecimal.ZERO;

        for (PackageProduct pp : p.getPackageProducts()) {
            Product product = pp.getProduct();

            count += pp.getQuantity();
            tSize = tSize.add(product.getVol().multiply(new BigDecimal(pp.getQuantity())));
        }

        String comment = MessageFormat.format("入库单：{0}，一共 {1} 件货品，总体积 {2}", p.getReferenceNumber(), count, tSize);
        log.info(comment);
        switch (costSubjectSnapshotVo.getSjt()) {
            case "SJ-AS":       //按体积
                total = tSize.multiply(costSubjectSnapshotVo.getSjv());
                break;
            case "SJ-AJ":       //按件
                total = costSubjectSnapshotVo.getSjv().multiply(new BigDecimal(count));
                break;
            case "SJ-AF":       //免费
                break;
        }

        total = total.setScale(2, RoundingMode.FLOOR);
        log.info("总价：{}", total);

        statements.setComment(comment);
        statements.setTotal(total);
        return statements;
    }

    @Transactional(readOnly = true)
    public Map<String, Statements> findUnpayStatementsByUserIdAndType(long userId, List<StatementTypeEnum> statementTypeEnum) {
        List<Statements> statements = statementsRepository.findByUserIdAndStatusAndTypeIn(userId, StatementStatusEnum.UNPAY, statementTypeEnum);

        Map<String, Statements> map = new HashMap<>();

        for (Statements s : statements) {
            map.put(s.getTarget(), s);
        }

        return map;
    }

    @Transactional(readOnly = true)
    public  List<Statements> findByUserIdAndTypeIn(long target, List<StatementTypeEnum> statementTypeEnum) {
        return statementsRepository.findByTargetAndTypeIn(String.valueOf(target), statementTypeEnum);
    }
}
