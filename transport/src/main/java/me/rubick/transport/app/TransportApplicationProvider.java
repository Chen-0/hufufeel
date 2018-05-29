package me.rubick.transport.app;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(basePackages = {"me.rubick.transport.app"})
@EntityScan("me.rubick.transport.app.model")
@EnableJpaRepositories("me.rubick.transport.app.repository")
@Configuration("me.rubick.transport.app.config")
@EnableSpringDataWebSupport
@EnableWebMvc
@EnableWebSecurity
public class TransportApplicationProvider {
    public static void main(String[] args) {
        SpringApplication.run(TransportApplicationProvider.class, args);
    }
}
