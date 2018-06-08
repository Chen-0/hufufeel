package me.rubick.hufu.logistics.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@Configuration("me.rubick.hufu.logistics.app.config")
@EnableJpaRepositories("me.rubick.hufu.logistics.app.repository")
@EntityScan("me.rubick.hufu.logistics.app.model")
@ComponentScan(basePackages = { "me.rubick.hufu.logistics.app" })
@EnableSpringDataWebSupport
public class HuFuLogisticsApplicationProvider {
	public static void main(String[] args) {
		SpringApplication.run(HuFuLogisticsApplicationProvider.class, args);
	}
}