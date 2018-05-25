package me.rubick.hufu.logistics.app.config;

import javax.annotation.Resource;

import me.rubick.hufu.logistics.app.model.Company;
import me.rubick.hufu.logistics.app.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;

import me.rubick.hufu.logistics.app.service.UserService;

/**
 * 权限配置
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(SpringSecurityConfig.class);

    @Resource
    private UserService userService;

    @Resource
    private CompanyService companyService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 权限控制
        http.authorizeRequests().antMatchers("/static/**", "/file/**").permitAll().antMatchers("/login", "/register")
                .anonymous()

                // 所有页面必须要登录才能进入，除了登录页面（/login）和注册页面（/register）
                .antMatchers("/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USERS")

                // 后台管理的页面需要管理员才能登录
                .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")

                .and().formLogin().loginPage("/login").loginProcessingUrl("/perform_login")
                .usernameParameter("username").passwordParameter("password").and().logout().logoutSuccessUrl("/").and()
                .rememberMe().rememberMeParameter("remember_me").key("chenjz_remember");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    /**
     * 用户登录判断
     *
     * @return
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Company company = companyService.findByUsername(username);

                if (ObjectUtils.isEmpty(company)) {
                    throw new UsernameNotFoundException("用户名或密码错误");
                }

                return company;
            }
        };
    }

    /**
     * 选择加密策略器
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                MessageDigestPasswordEncoder messageDigestPasswordEncoder = new Md5PasswordEncoder();
                return messageDigestPasswordEncoder.encodePassword(rawPassword.toString(), null);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                MessageDigestPasswordEncoder messageDigestPasswordEncoder = new Md5PasswordEncoder();
                return messageDigestPasswordEncoder.isPasswordValid(encodedPassword, rawPassword.toString(), null);
            }
        };
    }
}
