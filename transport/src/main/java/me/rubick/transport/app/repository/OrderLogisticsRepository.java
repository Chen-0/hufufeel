package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.OrderLogistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLogisticsRepository extends JpaRepository<OrderLogistics, Long> {

    OrderLogistics findTopByOrderId(long orderId);
}
