package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import me.rubick.hufu.logistics.app.model.Finance;

import java.util.List;

public interface FinanceRepository extends JpaRepository<Finance, Integer> {
    @Query("select sum(f.cost) from Finance f where f.userid = ?1 and YEAR(f.time) =?2 and MONTH(f.time) =?3")
    Double sumCostByUserIdAndTime(Integer userId, Integer year, Integer month);

    @Query("select f from Finance f where f.userid = ?1 and YEAR(f.time) = ?2 and MONTH(f.time) = ?3")
    List<Finance> getFinanceByUserIdAndTime(Integer userId, Integer year, Integer month);
}
