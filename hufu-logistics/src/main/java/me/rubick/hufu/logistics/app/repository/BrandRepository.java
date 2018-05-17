package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.rubick.hufu.logistics.app.model.Brand;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

    List<Brand> findByfatherid(Integer fatherId);
}
