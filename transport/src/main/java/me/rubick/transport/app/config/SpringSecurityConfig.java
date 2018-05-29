package me.rubick.transport.app.config;

import me.rubick.transport.app.model.User;
import me.rubick.transport.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.util.ObjectUtils;


@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //启用 csrf
        http.csrf().csrfTokenRepository(new HttpSessionCsrfTokenRepository());

//        http.authorizeRequests().anyRequest().permitAll();
        http.authorizeRequests()
                .antMatchers("/static/**", "/file/**").permitAll()
                .antMatchers("/login", "/register").anonymous()
                .antMatchers("/", "/index", "/contact_us","/strategy", "/qa", "/cost", "/about_us").permitAll()

                .antMatchers("/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USERS", "ROLE_DEFAULT")

                .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .usernameParameter("username")
                .passwordParameter("password")
                .and().logout().logoutSuccessUrl("/")
                .and()
                .rememberMe().rememberMeParameter("remember_me").key("F_enjsmyrh").rememberMeCookieName("F_werbuzemcihrn").rememberMeCookieDomain("hufufeel.com");
    }

    @Override
    protected void configure(
            AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username)
                    throws UsernameNotFoundException {
                User user = userService.getByUsername(username);

                if (ObjectUtils.isEmpty(user)) {
                    throw new UsernameNotFoundException("用户名或密码错误");
                }

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
