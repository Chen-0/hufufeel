package me.rubick.hufu.logistics.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@ComponentScan(basePackages = { "me.rubick.hufu.logistics.app" })
@EntityScan("me.rubick.hufu.logistics.app.model")
@EnableJpaRepositories("me.rubick.hufu.logistics.app.repository")
@Configuration("me.rubick.hufu.logistics.app.config")
@EnableSpringDataWebSupport
public class ApplicationProvider {
	public static void main(String[] args) {
		SpringApplication.run(ApplicationProvider.class, args);
	}
}