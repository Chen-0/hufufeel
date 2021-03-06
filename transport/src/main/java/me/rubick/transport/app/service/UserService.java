package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.FormException;
import me.rubick.common.app.helper.FormHelper;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
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

    public User findOne(long id) {
        User user = userRepository.findOne(id);
        user.setUserCsVo(JSONMapper.fromJson(user.getCsInfo(), UserCsVo.class));
        return user;
    }

    public List<User> findByIdIn(Collection<Long> collection) {
        return userRepository.findByIdIn(collection);
    }

    public User getByUsername(String name) {
        User user = userRepository.findByUsername(name);

        if (ObjectUtils.isEmpty(user)) {
            return null;
        }

        user.setUserCsVo(JSONMapper.fromJson(user.getCsInfo(), UserCsVo.class));
        return user;
    }

    public User createUser(User user) throws FormException {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityRepository.getOne(3));
        user.setAuthorities(authorities);
        return saveUser(user);
    }

    private User saveUser(User user) throws FormException {
        FormHelper formHelper = FormHelper.getInstance();

        if (userRepository.countByUsername(user.getUsername()) != 0) {
            formHelper.addError("username", "该登陆账号已被注册");
        }

        formHelper.hasError();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void resetPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Resource
    private PayService payService;

    public void chargeUser(User user, BigDecimal b) {
        user.setUsd(user.getUsd().add(b));
        userRepository.save(user);

        try {
            payService.createPaymentForSystem(user, b);
        } catch (BusinessException e) {
            log.error("", e);
        }
    }

    public String generateSn() {
        String sn = "10" + HashUtils.generateNumberString(4);
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
        user.setCsInfo(JSONMapper.toJSON(user.getUserCsVo()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll(String authority, String keyword) {

        return userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<User, Role> roleJoin = root.join("authorities", JoinType.INNER);

                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(roleJoin.get("authority"), authority));

                if (StringUtils.hasText(keyword)) {
                    String _keyword = "%" + keyword + "%";
                    predicates.add(cb.or(
                            cb.like(root.get("username"), _keyword),
                            cb.like(root.get("hwcSn"), _keyword),
                            cb.like(root.get("name"), _keyword)
                    ));
                }

                return cb.and(
                        predicates.toArray(new Predicate[]{})
                );
            }
        });
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
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

        if (statements == null || statements.size() == 0) {
            user.setArrearage(false);
        } else {
            user.setArrearage(true);
        }
        userRepository.save(user);
    }
}
