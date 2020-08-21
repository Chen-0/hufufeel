package me.rubick.transport.app.repository;

import me.rubick.transport.app.constants.PaymentStatusEnum;
import me.rubick.transport.app.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findTopByIdAndUserIdAndStatus(long id, long userId, PaymentStatusEnum status);

    Payment findTopByOutTradeNo(String outTradeNo);

    int countByOutTradeNo(String outTradeNo);

    Page<Payment> findByUserIdAndStatus(long userId, PaymentStatusEnum paymentStatusEnum, Pageable pageable);
}
