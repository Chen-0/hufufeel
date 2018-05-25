package me.rubick.hufu.logistics.app.repository;

import me.rubick.hufu.logistics.app.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUsername(String username);
}
