package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import me.rubick.hufu.logistics.app.model.Paymoney;

import java.util.List;

public interface PaymoneyRepository extends JpaRepository<Paymoney, Integer> {
    @Query("select SUM(p.money) from Paymoney p where YEAR(p.time)=?2 and MONTH(p.time)=?3 and p.userid=?1 and p.ordstatus = 1")
    Double sumMoneyByUserIdAndTime(Integer userId, Integer year, Integer month);

    @Query("select p from Paymoney p where p.userid=?1 and YEAR(p.time)=?2 and MONTH(p.time)=?3 and p.ordstatus=1)")
    List<Paymoney> getPaymenyByUserIdAndTime(Integer userId, Integer year, Integer month);
}
