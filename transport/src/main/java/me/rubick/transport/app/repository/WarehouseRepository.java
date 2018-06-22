package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Warehouse findTopByNameAndVisible(String name, boolean v);

    List<Warehouse> findAllByVisible(boolean v);
}
