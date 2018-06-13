package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.CostSubject;
import me.rubick.transport.app.model.Role;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.CostSubjectRepository;
import me.rubick.transport.app.repository.RoleRepository;
import me.rubick.transport.app.repository.UserRepository;
import me.rubick.transport.app.vo.CostSubjectSnapshotVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjiazhuo on 2017/9/14.
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
@Slf4j
public class UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private CostSubjectRepository costSubjectRepository;

    public User get(long id) {
        return userRepository.getOne(id);
    }

    public User getByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public void createUser(User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.getOne(1L));
        saveUser(user);
    }

    public void createAdmin(User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.getOne(1L));
        roles.add(roleRepository.getOne(2L));
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
                return user;
            }
        }

        return null;
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll(String authority) {

        return userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<User, Role> roleJoin = root.join("authorities", JoinType.INNER);

                return cb.and(
                        cb.equal(roleJoin.get("authority"), authority)
                );
            }
        });
    }

    public void storeCostSubject(User user, CostSubjectSnapshotVo costSubjectSnapshotVo) {
        CostSubject costSubject = costSubjectRepository.findTopByUserId(user.getId());

        if (ObjectUtils.isEmpty(costSubject)) {
            costSubject = new CostSubject();
            costSubject.setUserId(user.getId());
        }

        costSubject.setCostSubjectSnapshot(JSONMapper.toJSON(costSubjectSnapshotVo));
        costSubjectRepository.save(costSubject);
    }

    public CostSubjectSnapshotVo findCostSubjectByUserId(User user) {
        CostSubject costSubject = costSubjectRepository.findTopByUserId(user.getId());
        if (ObjectUtils.isEmpty(costSubject)) {
            return null;
        }
        return JSONMapper.fromJson(costSubject.getCostSubjectSnapshot(), CostSubjectSnapshotVo.class);
    }
}
