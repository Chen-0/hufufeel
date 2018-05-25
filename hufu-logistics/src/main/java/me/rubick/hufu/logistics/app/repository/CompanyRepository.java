package me.rubick.hufu.logistics.app.repository;

import me.rubick.hufu.logistics.app.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jiazhuo on 2017/3/17.
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findOneByUsername(String username);
}
