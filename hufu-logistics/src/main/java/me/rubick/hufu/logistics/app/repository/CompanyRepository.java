package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.rubick.hufu.logistics.app.model.Company;

/**
 * Created by Jiazhuo on 2017/3/17.
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findOneByUsername(String username);
}
