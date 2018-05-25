package me.rubick.hufu.logistics.app.repository;

import me.rubick.hufu.logistics.app.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

    List<Brand> findByfatherid(Integer fatherId);
}
