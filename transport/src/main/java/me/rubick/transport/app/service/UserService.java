package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.HashUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.StatementStatusEnum;
import me.rubick.transport.app.constants.StatementTypeEnum;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.AuthorityRepository;
import me.rubick.transport.app.repository.CostSubjectRepository;
import me.rubick.transport.app.repository.StatementsRepository;
import me.rubick.transport.app.repository.UserRepository;
import me.rubick.transport.app.vo.CostSubjectSnapshotVo;
import me.rubick.transport.app.vo.UserCsVo;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    private StatementsRepository statementsRepository;

    @Resource
    private AuthorityRepository authorityRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private CostSubjectRepository costSubjectRepository;

    public User get(long id) {
        return userRepository.getOne(id);
    }

    public List<User> findByIdIn(Collection<Long> collection) {
        return userRepository.findByIdIn(collection);
    }

    public User getByUsername(String name) {
        User user = userRepository.findByUsername(name);
        user.setUserCsVo(JSONMapper.fromJson(user.getCsInfo(), UserCsVo.class));
        return user;
    }

    public User createUser(User user) {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityRepository.getOne(3));
        user.setAuthorities(authorities);
        return saveUser(user);
    }

    private User saveUser(User user) {
        Assert.hasText(user.getUsername(), "登陆账号不能为空");
        Assert.hasText(user.getPassword(), "密码不能为空");
        Assert.hasText(user.getName(), "用户昵称不能为空");
        Assert.isTrue(userRepository.countByUsername(user.getUsername()) == 0, "该登陆账号已被注册");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setHwcSn(generateSn());
        return userRepository.save(user);
    }

    private String generateSn() {
        String sn = HashUtils.generateNumberString(4);
        do {
            if (userRepository.countByHwcSn(sn) == 0) {
                return sn;
            }
        } while (true);
    }

    public User getByLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!ObjectUtils.isEmpty(auth)) {
            Object object = auth.getPrincipal();
            if (!ObjectUtils.isEmpty(object) && object instanceof User) {
                User user = (User) object;
                return userRepository.findOne(user.getId());
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

    public CostSubjectSnapshotVo findCostSubjectByUserId(long userId) {
        CostSubject costSubject = costSubjectRepository.findTopByUserId(userId);
        if (ObjectUtils.isEmpty(costSubject)) {
            return null;
        }
        return JSONMapper.fromJson(costSubject.getCostSubjectSnapshot(), CostSubjectSnapshotVo.class);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean payUSD(long userId, BigDecimal usd) {
        //小于 0.01 直接支付成功
        if (usd.compareTo(new BigDecimal("0.01")) < 0) {
            return true;
        }

        int row = userRepository.payUSD(userId, usd);
        log.info("payUSD::userId={}, usd={} ---- row={}", userId, usd, row);
        return row == 1;
    }


    public void updateUserFreeze(long userId) {
        User user = userRepository.findOne(userId);
        List<Statements> statements = statementsRepository.findByUserIdAndStatusAndTypeIn(
                user.getId(),
                StatementStatusEnum.UNPAY,
                Arrays.asList(StatementTypeEnum.STORE)
        );

        if (ObjectUtils.isEmpty(statements)) {
            user.setArrearage(false);
        } else {
            user.setArrearage(true);
        }
        userRepository.save(user);
    }
}
