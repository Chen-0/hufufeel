package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import me.rubick.hufu.logistics.app.model.UserStatistics;

import java.util.List;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Integer> {

    UserStatistics findByUserIdAndTime(Integer userId, Integer time);

    @Query("select u from UserStatistics u where  u.time = ?1 and (u.in > 0 or u.out > 0)")
    List<UserStatistics> findByTime(Integer time);
}
