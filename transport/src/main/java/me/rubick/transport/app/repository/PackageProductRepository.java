package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.PackageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface PackageProductRepository extends JpaRepository<PackageProduct, Long> {

    @Modifying
    @Query("update PackageProduct p set p.quantity = ?3 where p.packageId=?1 and p.productId=?2")
    void inbound(long packageId, long productId, int qty);

    @Modifying
    @Query("update PackageProduct p set p.quantity = p.quantity + ?3 where p.packageId=?1 and p.productId=?2 and p.quantity + ?3 <= p.expectQuantity")
    void inboundReject(long packageId, long productId, int qty);

    @Query("select sum(pp.quantity * p.weight) from PackageProduct pp, Product p where pp.productId = p.id and pp.packageId = ?1")
    BigDecimal sumWeight(long packageId);
}
