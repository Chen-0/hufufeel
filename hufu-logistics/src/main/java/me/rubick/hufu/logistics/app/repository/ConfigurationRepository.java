package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.rubick.hufu.logistics.app.model.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {


}
