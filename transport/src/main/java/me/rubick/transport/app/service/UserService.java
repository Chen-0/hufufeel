package me.rubick.transport.app.service;

import me.rubick.transport.app.model.Role;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.RoleRepository;
import me.rubick.transport.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjiazhuo on 2017/9/14.
 */
@Service
@Transactional
public class UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public User get(long id) {
        return userRepository.getOne(id);
    }

    public User getByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public void createUser(User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.getOne(1L));

        user.setAuthorities(roles);
        saveUser(user);
    }

    public void createAdmin(User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.getOne(1L));
        roles.add(roleRepository.getOne(2L));

        user.setAuthorities(roles);
        saveUser(user);
    }

    private void saveUser(User user) {
        Assert.hasText(user.getUsername(), "邮箱不能为空");
        Assert.hasText(user.getPassword(), "密码不能为空");
        Assert.isTrue(userRepository.countByUsername(user.getUsername()) == 0, "该邮箱已被注册");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public User getByLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!ObjectUtils.isEmpty(auth)) {
            Object object = auth.getPrincipal();
            if (!ObjectUtils.isEmpty(object) && object instanceof User) {
                User user = (User) object;
                logger.info("Login User: {}", user.getUsername());
                return userRepository.findOne(user.getId());
            }
        }

        return null;
    }

    public User update(User user) {
        return userRepository.save(user);
    }
}
