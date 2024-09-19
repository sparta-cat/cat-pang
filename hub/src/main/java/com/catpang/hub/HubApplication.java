package com.catpang.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.catpang.hub", "com.catpang.core", "com.catpang.address"})
@EnableFeignClients
@EntityScan(basePackages = {"com.catpang.hub.domain.model", "com.catpang.address.domain.model"})  // 모든 엔티티를 스캔
@EnableJpaRepositories(basePackages = {"com.catpang.hub.domain.repository", "com.catpang.address.domain.repository"})
public class HubApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubApplication.class, args);
	}
}
