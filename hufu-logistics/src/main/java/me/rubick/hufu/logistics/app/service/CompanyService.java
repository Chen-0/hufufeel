package me.rubick.hufu.logistics.app.service;

import me.rubick.hufu.logistics.app.model.Company;
import me.rubick.hufu.logistics.app.model.CompanyRExpress;
import me.rubick.hufu.logistics.app.repository.AuthorityRepository;
import me.rubick.hufu.logistics.app.repository.CompanyExpressRepository;
import me.rubick.hufu.logistics.app.repository.CompanyRExpressRepository;
import me.rubick.hufu.logistics.app.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import me.rubick.hufu.logistics.app.model.CompanyExpress;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CompanyService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CompanyRExpressRepository companyRExpressRepository;

    @Resource
    private CompanyExpressRepository companyExpressRepository;

    @Resource
    private CompanyRepository companyRepository;

    @Resource
    private AuthorityRepository authorityRepository;

    public List<CompanyExpress> getAllCompanyExpress() {
        return companyRExpressRepository.findAll();
    }

    public CompanyExpress findCompanyExpress(Integer id) {
        return companyRExpressRepository.findOne(id);
    }

    public CompanyExpress storeCompanyExpress(CompanyExpress companyExpress) {
        return companyRExpressRepository.save(companyExpress);
    }

    public void storeCompanyRExpress(CompanyRExpress companyRExpress) {
        companyExpressRepository.save(companyRExpress);
    }

    public Company findOne(Integer id) {
        return companyRepository.findOne(id);
    }

    public Company storeCompany(Company company) {
        company.setAuthorities(authorityRepository.findByAuthority("ROLE_USERS"));
        return companyRepository.save(company);
    }

    public CompanyRExpress findCompanyRExpress(Integer id) {
        return companyExpressRepository.findOne(id);
    }

    public Company findByUsername(String username) {
        return companyRepository.findOneByUsername(username);
    }

    public Company getLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Company) auth.getPrincipal();
    }

    public Boolean isAdminWithLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((Company) auth.getPrincipal()).isAdmin();
    }
}
