package me.rubick.hufu.logistics.app.repository;

import me.rubick.hufu.logistics.app.model.CompanyRExpress;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jiazhuo on 2017/3/16.
 */
public interface CompanyExpressRepository extends JpaRepository<CompanyRExpress, Integer> {

    CompanyRExpress findByCompanyIdAndExpressId(Integer companyId, Integer expressId);
}
