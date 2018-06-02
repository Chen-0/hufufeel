package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.PackageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface PackageProductRepository extends JpaRepository<PackageProduct, Long> {

    @Modifying
    @Query("update PackageProduct p set p.realWeight = ?3, p.realQty = ?4 where p.packageId=?1 and p.productId=?2")
    void inbound(long packageId, long productId, BigDecimal weight, int qty);
}
