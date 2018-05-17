package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import me.rubick.hufu.logistics.app.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByCustomer(String customer);

    @Query("select u from User u where (u.customer like %?1% or u.userna like %?1% or u.username like %?1%) order By u.id desc")
    Page<User> findByCustomerLike(String customer, Pageable pageable);


    User findByUsername(String username);

}
