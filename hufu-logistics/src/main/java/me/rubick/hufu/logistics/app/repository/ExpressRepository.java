package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.rubick.hufu.logistics.app.model.Express;

public interface ExpressRepository extends JpaRepository<Express, Integer> {


}
