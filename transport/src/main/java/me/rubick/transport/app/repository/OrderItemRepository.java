package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select sum(oi.quantity * p.weight) from OrderItem oi, Product p where  oi.productId = p.id and oi.orderId = ?1")
    BigDecimal sumWeight(long orderId);
}
