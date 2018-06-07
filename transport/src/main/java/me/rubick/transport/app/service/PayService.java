package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.transport.app.model.Payment;
import me.rubick.transport.app.model.PaymentStatus;
import me.rubick.transport.app.model.PaymentType;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.PaymentRepository;
import me.rubick.transport.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class PayService {

    @Resource
    private PaymentRepository paymentRepository;

    @Resource
    private UserRepository userRepository;

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
        user.setMoney(user.getMoney().add(payment.getTotalFee()));
        userRepository.save(user);
    }
}
