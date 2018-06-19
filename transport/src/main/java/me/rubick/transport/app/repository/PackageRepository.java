package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.OrderStatusEnum;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.model.PackageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PackageRepository extends JpaRepository<Package, Long>, JpaSpecificationExecutor<Package> {

    @Query("select max(p.sn) from Package p where p.sn like %?1%")
    public String getMaxSN(String date);

    long countByUserIdAndStatus(long userId, PackageStatus packageStatus);
}
