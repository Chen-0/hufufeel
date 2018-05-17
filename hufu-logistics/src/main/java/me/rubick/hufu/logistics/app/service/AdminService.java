package me.rubick.hufu.logistics.app.service;

import org.springframework.stereotype.Service;
import me.rubick.hufu.logistics.app.model.Admin;
import me.rubick.hufu.logistics.app.repository.AdminRepository;

import javax.annotation.Resource;

/**
 * Created by Jiazhuo on 2017/3/14.
 */
@Service
public class AdminService {
    @Resource
    private AdminRepository adminRepository;

    Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
}
