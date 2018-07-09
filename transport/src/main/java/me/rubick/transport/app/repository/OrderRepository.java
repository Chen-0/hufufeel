package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Order;
import me.rubick.transport.app.constants.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("select max(p.sn) from Order p where p.sn like %?1%")
    public String getMaxSN(String date);

    long countByUserIdAndStatus(long userId, OrderStatusEnum orderStatusEnum);

    int countBySn(String sn);

    Order findTopBySn(String sn);
}
