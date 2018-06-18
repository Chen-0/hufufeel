package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
