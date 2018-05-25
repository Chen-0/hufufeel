package me.rubick.hufu.logistics.app.repository;

import me.rubick.hufu.logistics.app.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    List<Authority> findByAuthority(String s);
}
