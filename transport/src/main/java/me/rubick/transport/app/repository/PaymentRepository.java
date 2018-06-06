package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Payment;
import me.rubick.transport.app.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findTopByIdAndUserIdAndStatus(long id, long userId, PaymentStatus status);

    Payment findTopByOutTradeNo(String outTradeNo);

    int countByOutTradeNo(String outTradeNo);
}
