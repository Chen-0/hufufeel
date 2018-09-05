package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.transport.app.constants.*;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.*;
import me.rubick.transport.app.vo.CostSubjectSnapshotVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private OrderService orderService;


    public Payment findPayment(long paymentId, User user) {
        return paymentRepository.findTopByIdAndUserIdAndStatus(paymentId, user.getId(), PaymentStatusEnum.FALSE);
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
        payment.setStatus(PaymentStatusEnum.FALSE);
        payment.setTotalFee(totalFee);
        payment.setType(PaymentTypeEnum.ALIPAY);

        return paymentRepository.save(payment);
    }

    public Payment createPaymentForSystem(User user, BigDecimal totalFee) throws BusinessException {
        totalFee = totalFee.setScale(2, RoundingMode.FLOOR);
        log.info("createPaymentForSystem - total={}", totalFee);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("[A001] 禁止访问！");
        }

        Payment payment = new Payment();
        payment.setUserId(user.getId());
        payment.setExtendsInfo("");
        payment.setOutTradeNo(generateOutTradeNo());
        payment.setStatus(PaymentStatusEnum.TRUE);
        payment.setSuccessAt(new Date());
        payment.setTotalFee(totalFee);
        payment.setType(PaymentTypeEnum.SYSTEM);
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
        payment.setStatus(PaymentStatusEnum.TRUE);
        payment.setSuccessAt(new Date());
        paymentRepository.save(payment);

        switch (payment.getType()) {
            case ALIPAY:
                successForAccount(payment);
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

    public Page<Statements> findAllStatements(Long userId, Date start, Date end, Pageable pageable) {

        return statementsRepository.findAll(new Specification<Statements>() {
            @Override
            public Predicate toPredicate(Root<Statements> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicates = new ArrayList<>(10);

                if (!ObjectUtils.isEmpty(userId)) {
                    predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
                }

                if (!ObjectUtils.isEmpty(start)) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), start));
                }

                if (!ObjectUtils.isEmpty(end)) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), end));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        }, pageable);
//        return statementsRepository.findByUserId(userId, pageable);
    }

    public Statements saveStatements(Statements statements, BigDecimal total) {
        log.info("系统自动生成 = {}, 提交={}", statements.getTotal(), total);
        if (!ObjectUtils.isEmpty(total)) {
            statements.setTotal(total);
        }
        return statementsRepository.save(statements);
    }

    /**
     * 计算费用
     *
     * @param p
     * @return
     */
    public Statements calcCK(Package p, BigDecimal t_weight) {

        if (p.getType().equals(PackageTypeEnum.REJECT)) {
            return calcTHRK(p);
        }

        CostSubjectSnapshotVo costSubjectSnapshotVo = userService.findCostSubjectByUserId(p.getUserId());

        Statements statements = new Statements();
        statements.setUserId(p.getUserId());
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.RK);
        statements.setTarget(String.valueOf(p.getId()));
        statements.setPayAt(null);

        BigDecimal total = BigDecimal.ZERO;

        String comment = MessageFormat.format("入库单：{0} ，总重：{1} KG", p.getSn(), t_weight);

        switch (costSubjectSnapshotVo.getRkt()) {
            case "RK-AZ":       //按重
                total = t_weight.multiply(costSubjectSnapshotVo.getRkv());
                log.info("userId={},按重入库, 总重：{}", p.getUserId(), t_weight);
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

    private Statements calcTHRK(Package p) {
        CostSubjectSnapshotVo costSubjectSnapshotVo = userService.findCostSubjectByUserId(p.getUserId());

        Statements statements = new Statements();
        statements.setUserId(p.getUserId());
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.RK);
        statements.setTarget(String.valueOf(p.getId()));
        statements.setPayAt(null);

        BigDecimal total = BigDecimal.ZERO;

        switch (costSubjectSnapshotVo.getThrkt()) {
            case "TH_RK_AD":       //按单收费
                total = total.add(costSubjectSnapshotVo.getThrkv());
                break;
        }

        log.info("总价：{}", total);
        String comment = MessageFormat.format("退货单：{0} 正在入库！产生费用为：{1} USD", p.getSn(), total);
        statements.setComment(comment);
        statements.setTotal(total);
        return statements;
    }

    private Statements calcTHSJ(Package p) {
        CostSubjectSnapshotVo costSubjectSnapshotVo = userService.findCostSubjectByUserId(p.getUserId());

        Statements statements = new Statements();
        statements.setUserId(p.getUserId());
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.RK);
        statements.setTarget(String.valueOf(p.getId()));
        statements.setPayAt(null);

        BigDecimal total = BigDecimal.ZERO;

        switch (costSubjectSnapshotVo.getThsjt()) {
            case "TH_SJ_SL":       //按件收费
                total = total.add(costSubjectSnapshotVo.getThsjv().multiply(new BigDecimal(p.getQuantity())));
                break;
        }

        log.info("总价：{}", total);
        String comment = MessageFormat.format("退货单：{0} 正在上架！产生费用为：{1} USD", p.getSn(), total);
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

    public boolean  payStatements(Collection<Statements> items) {
        boolean flag = true;

        for (Statements statements : items) {
            if (statements.getStatus().equals(StatementStatusEnum.UNPAY)) {
                if (userService.payUSD(statements.getUserId(), statements.getTotal())) {
                    //支付成功
                    statements.setStatus(StatementStatusEnum.PAY);
                    statements.setPayAt(new Date());
                    statementsRepository.save(statements);
                } else {
                    flag = false;
                }
            }
        }

        return flag;
    }

    public Statements secPayStatements(long id, User user) throws BusinessException {
        Statements statements = statementsRepository.findOne(id);

        if (user.getId() != statements.getUserId()) {
            throw new BusinessException("您没有权限执行该操作！");
        }

        if (statements.getStatus().equals(StatementStatusEnum.UNPAY)) {
            if (userService.payUSD(statements.getUserId(), statements.getTotal())) {
                //支付成功
                statements.setStatus(StatementStatusEnum.PAY);
                statements.setPayAt(new Date());
                statementsRepository.save(statements);

                switch (statements.getType()) {
                    case RK: {
                        Package p = packageRepository.findOne(Long.valueOf(statements.getTarget()));
                        if (p.getNextStatus() != PackageStatusEnum.NULL) {
                            p.setStatus(p.getNextStatus());
                            p.setNextStatus(PackageStatusEnum.NULL);
                            packageRepository.save(p);
                        }
                        break;
                    }
                    case SJ: {
                        Package p = packageRepository.findOne(Long.valueOf(statements.getTarget()));
                        if (p.getNextStatus() != PackageStatusEnum.NULL) {
                            p.setStatus(p.getNextStatus());
                            p.setNextStatus(PackageStatusEnum.NULL);
                            packageRepository.save(p);

                            stockService.addStock(p);
                        }
                        break;
                    }
                    case ORDER: {
                        int count = statementsRepository.countByUserIdAndStatusAndTypeInAndTarget(
                                statements.getUserId(),
                                StatementStatusEnum.UNPAY,
                                Arrays.asList(StatementTypeEnum.ORDER),
                                statements.getTarget()
                        );

                        if (count == 0) {
                            Order order = orderRepository.findOne(Long.valueOf(statements.getTarget()));
                            if (order.getNextStatus() != OrderStatusEnum.NULL) {
                                order.setStatus(order.getNextStatus());
                                order.setNextStatus(OrderStatusEnum.NULL);
                                order.setOutTime(new Date());
                                orderRepository.save(order);
                            }
                        }

                    }
                    case STORE: {
                        userService.updateUserFreeze(statements.getUserId());
                    }
                }
            } else {
                throw new BusinessException("余额不足");
            }
        }
        return statements;
    }

    /**
     * 上架费
     *
     * @param p
     * @return
     */
    @Transactional(readOnly = true)
    public Statements calcSJ(Package p) {
        if (p.getType().equals(PackageTypeEnum.REJECT)) {
            return calcTHSJ(p);
        }

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

        String comment = MessageFormat.format("入库单：{0}，一共 {1} 件货品，总体积 {2}", p.getSn(), count, tSize);
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
    public List<Statements> findByUserIdAndTypeIn(long target, List<StatementTypeEnum> statementTypeEnum) {
        return statementsRepository.findByTargetAndTypeIn(String.valueOf(target), statementTypeEnum);
    }

    public Statements createORDER_S(Order order, BigDecimal surcharge, String surchargeComment) {
        Statements statements = new Statements();
        statements.setUserId(order.getUserId());
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.ORDER);
        statements.setTarget(String.valueOf(order.getId()));
        statements.setPayAt(null);
        statements.setComment("运单额外收费原因：" + surchargeComment);
        statements.setTotal(surcharge);
        return statements;
    }

    private Statements create(long userId, Long targetId, StatementTypeEnum type, BigDecimal total, String comment) {
        Statements statements = new Statements();
        statements.setUserId(userId);
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(type);
        statements.setTarget(String.valueOf(targetId));
        statements.setPayAt(null);
        statements.setComment(comment);
        statements.setTotal(total);
        return statements;
    }

    public Statements createWithSave(long userId, String target, String comment, BigDecimal total) {
        Statements statements = new Statements();
        statements.setUserId(userId);
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.ORDER);
        statements.setTarget(target);
        statements.setPayAt(null);
        statements.setComment(comment);
        statements.setTotal(total);
        return statementsRepository.save(statements);
    }

    public Statements createORDER(Order order) {
        Statements statements = new Statements();
        statements.setUserId(order.getUserId());
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.ORDER);
        statements.setTarget(String.valueOf(order.getId()));
        statements.setPayAt(null);

        BigDecimal total = BigDecimal.ZERO;

        List<BigDecimal> list = getOrderTotalDetail(order);

        for (BigDecimal b: list) {
            total = total.add(b);
        }


        //总重、总体积
        int count = 0;
        BigDecimal tWeight = BigDecimal.ZERO;

        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();

            count += orderItem.getQuantity();
            tWeight = tWeight.add(product.getWeight().multiply(new BigDecimal(orderItem.getQuantity())));
        }

        log.info("orderId={}, count={}, total weight={}", order.getId(), count, tWeight);
        StringBuilder _ext = new StringBuilder("");

        if (list.size() == 1) {
            _ext.append(MessageFormat.format("按件收费：{1}USD。", list.get(0)));
        } if (list.size() == 2) {
            _ext.append(MessageFormat.format("重量收费：{0}USD，超件费：{1}USD。", list.get(0), list.get(1)));
        }

        String comment = MessageFormat.format("出库单：{0}，一共 {1} 件货品，总重量 {2} KG。{3}", order.getSn(), count, tWeight, _ext);

        total = total.setScale(2, RoundingMode.FLOOR);
        log.info("总价：{}", total);

        statements.setComment(comment);
        statements.setTotal(total);
        return statements;
    }

    public List<BigDecimal> getOrderTotalDetail(Order order) {
        CostSubjectSnapshotVo costSubjectSnapshotVo = userService.findCostSubjectByUserId(order.getUserId());
        int count = 0;
        BigDecimal tWeight = BigDecimal.ZERO;
        List<BigDecimal> list = new ArrayList<>();

        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();

            count += orderItem.getQuantity();
            tWeight = tWeight.add(product.getWeight().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        switch (costSubjectSnapshotVo.getDdt()) {
            case "DD-AZ":       //按重
                count = count >= 10 ? 10 : count;
                count -= 1;

                if (tWeight.compareTo(new BigDecimal("1")) <= 0) {
                    //1kg 内含 1kg
                    list.add(costSubjectSnapshotVo.getDdv().get(0));
                    list.add(costSubjectSnapshotVo.getDdv().get(1).multiply(new BigDecimal(count)));
                } else {
                    //超过1kg
                    list.add(costSubjectSnapshotVo.getDdv().get(2).multiply(tWeight));
                    list.add(costSubjectSnapshotVo.getDdv().get(3).multiply(new BigDecimal(count)));
                }

                break;
            case "DD-AJ":       //按件
                list.add(costSubjectSnapshotVo.getDdv().get(0).multiply(new BigDecimal(count)));
                break;
        }

        return list;
    }

    public Statements createSTORECOST(User user, BigDecimal total) {
        Statements statements = new Statements();
        statements.setUserId(user.getId());
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.STORE);
        statements.setTarget(String.valueOf(user.getId()));
        statements.setPayAt(null);
        total = total.setScale(2, RoundingMode.FLOOR);
        statements.setTotal(total);
        statements.setComment("收取仓租费：" + total + "USD");

        return statementsRepository.save(statements);
    }

    /**
     * 运费
     * @param order
     * @param total
     * @return
     */
    public Statements createExpressFee(Order order, BigDecimal total) {
        Statements statements = new Statements();
        statements.setUserId(order.getUserId());
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.ORDER);
        statements.setTarget(String.valueOf(order.getId()));
        statements.setPayAt(null);
//        total = total.setScale(2, RoundingMode.FLOOR);
        statements.setTotal(total);
        statements.setComment("运费：" + total + "USD");

        return statementsRepository.save(statements);
    }

    /**
     * 补收费
     * @return
     */
    public Statements createRecharge(User user, BigDecimal total, String comment) {
        Statements statements = new Statements();

        statements.setUserId(user.getId());
        statements.setStatus(StatementStatusEnum.UNPAY);
        statements.setType(StatementTypeEnum.RECHARGE);
        statements.setTotal(total);
        statements.setTarget(null);
        statements.setComment(comment);
        statements.setPayAt(null);

        return statementsRepository.save(statements);
    }
}
