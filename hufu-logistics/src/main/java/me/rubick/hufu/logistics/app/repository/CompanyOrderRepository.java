package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import me.rubick.hufu.logistics.app.model.CompanyOrder;

import java.util.List;

/**
 * Created by Jiazhuo on 2017/3/15.
 */
public interface CompanyOrderRepository extends JpaRepository<CompanyOrder, Integer> {

    @Query("select count(o) from CompanyOrder o")
    Integer getTotal();

    @Query("select max(o.batch) from CompanyOrder o where o.batch like %?1%")
    String getMaxBatch(String batch);

    //    @Query("select o from CompanyOrder o where o.trackingNumber in (?1)")
    List<CompanyOrder> findByTrackingNumberIn(String[] trackingNumber);


    void deleteByTrackingNumberIn(String[] trackingNumbers);

    CompanyOrder findByTrackingNumber(String trackingNumber);

//    @Modifying
//    @Query("update CompanyOrder o set o.statusId = 2 where o.trackingNumber in ?1")
//    void updateOrderStatusTo2(String[] trackingNumber);

    @Query("SELECT count(*) from CompanyOrder o where MONTH(o.outTime) = ?1")
    Double getTotalAtMonth(Integer month);
}
