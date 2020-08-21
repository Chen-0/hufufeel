package me.rubick.transport.app.config;

import me.rubick.transport.app.service.SecurityService;
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
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.annotation.Resource;


@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //启用 csrf
        http.csrf().csrfTokenRepository(new HttpSessionCsrfTokenRepository()).ignoringAntMatchers("/pay/alipay/notify");

//        http.authorizeRequests().anyRequest().permitAll();
        http.authorizeRequests()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/static/**", "/file/**").permitAll()
                .antMatchers("/login", "/register").permitAll()
                .antMatchers("/api/noAuth/**").permitAll()
                .antMatchers("/error/**").permitAll()

                //支付回调
                .antMatchers("/pay/alipay/notify").permitAll()
                .antMatchers("/", "/index", "/contact_us","/strategy", "/qa", "/cost", "/about_us").permitAll()

                .antMatchers("/admin/**")
                .hasAnyAuthority("ROLE_ADMIN")
                
                .antMatchers("/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USERS", "ROLE_HWC")


                .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .usernameParameter("username")
                .passwordParameter("password")
                .and().logout().logoutSuccessUrl("/")
                .and().exceptionHandling().accessDeniedPage("/error/403")
                .and()
                .rememberMe().rememberMeParameter("remember_me").key("QWTtvidaThAmnBgn").rememberMeCookieName("zVaWBuPrLlDfXHlc");
    }

    @Override
    protected void configure(
            AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Resource
    private SecurityService securityService;

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username)
                    throws UsernameNotFoundException {
                UserDetails user = securityService.loadUserByUsername(username);

                return user;
            }
        };
    }

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
