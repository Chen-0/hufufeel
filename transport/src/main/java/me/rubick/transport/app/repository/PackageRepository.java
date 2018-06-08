package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PackageRepository extends JpaRepository<Package, Long>, JpaSpecificationExecutor<Package> {


}
