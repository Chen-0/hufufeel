package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.PackageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageProductRepository extends JpaRepository<PackageProduct, Long> {
}
