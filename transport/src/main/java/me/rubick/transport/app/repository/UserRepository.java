package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by chenjiazhuo on 2017/9/14.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name);

    long countByUsername(String username);
}
