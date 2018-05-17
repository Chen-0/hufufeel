package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.rubick.hufu.logistics.app.model.CompanyRExpress;

/**
 * Created by Jiazhuo on 2017/3/16.
 */
public interface CompanyExpressRepository extends JpaRepository<CompanyRExpress, Integer> {

    CompanyRExpress findByCompanyIdAndExpressId(Integer companyId, Integer expressId);
}
