package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.vo.CostSnapshotVo;
import me.rubick.transport.app.vo.CostSubjectSnapshotVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;


@Service("securityService")
@Slf4j
public class SecurityService implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.getByUsername(s);


        if (ObjectUtils.isEmpty(user)) {
            log.error("账号：{} - 用户名或密码错误。", s);
            throw new UsernameNotFoundException("用户名或密码错误");
        } else {

//            CostSubjectSnapshotVo costSubjectSnapshotVo = userService.findCostSubjectByUserId(user.getId());
//
//            if (ObjectUtils.isEmpty(costSubjectSnapshotVo)) {
//                log.error("账号：{} - 该用户没完成设置，请联系管理员。", s);
//                throw new UsernameNotFoundException("该用户没完成设置，请联系管理员。");
//            }

            return user;
        }
    }
}
