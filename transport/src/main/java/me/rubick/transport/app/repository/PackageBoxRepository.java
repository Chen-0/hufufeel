package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.PackageBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageBoxRepository extends JpaRepository<PackageBox, Long> {

    List<PackageBox> findAllByPackageId(long packageId);
}
