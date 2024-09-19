package com.catpang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.catpang.hubproduct", "com.catpang.core"})
@EntityScan(basePackages = {"com.catpang.hubproduct.domain.model", "com.catpang.core.domain.model"})
@EnableJpaRepositories(basePackages = {"com.catpang.hubproduct.domain.repository", "com.catpang.core.domain.repository"})
@EnableFeignClients(basePackages = {"com.catpang.hubproduct.infrastructure.feign", "com.catpang.core.infrastructure.feign"})
public class HubProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubProductApplication.class, args);
	}
}
