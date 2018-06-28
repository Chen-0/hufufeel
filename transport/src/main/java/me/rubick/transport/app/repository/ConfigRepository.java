package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConfigRepository extends JpaRepository<Config, Long> {

    List<Config> findByKey(String key);

    @Modifying
    @Query("update Config c set c.value = ?2 where c.key = ?1")
    int update(String key, String value);
}
