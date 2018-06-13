package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigRepository extends JpaRepository<Config, Long> {

    List<Config> findByKey(String key);
}
