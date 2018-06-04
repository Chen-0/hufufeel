package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByMenuKey(String menuKey, Sort sort);
}
