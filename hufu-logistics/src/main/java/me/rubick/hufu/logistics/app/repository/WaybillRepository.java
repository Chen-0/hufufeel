package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import me.rubick.hufu.logistics.app.model.Waybill;

import java.math.BigDecimal;
import java.util.List;

public interface WaybillRepository extends JpaRepository<Waybill, Integer> {

    @Query("select w from Waybill w where w.arrive=?1 and (w.trackingnumber like  %?2% or w.expressnum like %?2%) order by w.id desc")
    Page<Waybill> getWaybill(Integer arrive, String keyword, Pageable pageable);

    @Query("SELECT count(*) from Waybill w where MONTH(w.outtime) = ?1")
    Double getTotalAtMonth(Integer mouth);

    List<Waybill> findByTrackingnumberIn(String[] trackingNumber);

    Waybill findByTrackingnumber(String trackingNumber);

    @Query("select count(w) from Waybill w where w.userid=?1 and YEAR(w.outtime)=?2 and MONTH(w.outtime)=?3")
    Integer countByUserIdAndTime(Integer userId, Integer year, Integer month);


    @Query("select w from Waybill  w where w.userid=?2 and w.cost=?1")
    List<Waybill> findByCostAndUserid(BigDecimal cost, Integer id);
}
