package me.rubick.hufu.logistics.app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import me.rubick.hufu.logistics.app.model.Company;

import javax.annotation.Resource;

@Component
public class SecurityService implements UserDetailsService {


    @Resource
    private CompanyService companyService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return companyService.findByUsername(s);
    }
}
