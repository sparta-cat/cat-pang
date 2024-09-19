package com.catpang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.catpang.company", "com.catpang.core"})
@EnableFeignClients
@EntityScan(basePackages = {"com.catpang.company.domain.model"})
@EnableJpaRepositories(basePackages = {"com.catpang.company.domain.repository"})
public class CompanyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyApplication.class, args);
	}
}
