package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.constants.PackageStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Long>, JpaSpecificationExecutor<Package> {

    @Query("select max(p.sn) from Package p where p.sn like %?1%")
    public String getMaxSN(String date);

    @Query("select max(p.cn) from Package p where p.cn like %?1%")
    public String getMaxCN(String date);

    long countByUserIdAndStatus(long userId, PackageStatusEnum packageStatus);

    public List<Package> findAllByIsDelete(Boolean d);
}
