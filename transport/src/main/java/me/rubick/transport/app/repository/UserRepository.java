package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by chenjiazhuo on 2017/9/14.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String name);

    long countByUsername(String username);
}
