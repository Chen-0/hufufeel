package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Created by chenjiazhuo on 2017/9/14.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String name);

    long countByUsername(String username);

    @Modifying
    @Query("update User u set u.usd = u.usd - ?2 " +
            "where u.id = ?1 and u.usd - ?2 >= 0 ")
    int payUSD(long userId, BigDecimal usd);

    List<User> findByIdIn(Collection<Long> collection);

    int countByHwcSn(String hwcSn);
}
