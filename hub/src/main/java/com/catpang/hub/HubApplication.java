package com.catpang.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.catpang.hub", "com.catpang.core"})
@EnableFeignClients
public class HubApplication {

	public static void main(String[] args) {

		SpringApplication.run(HubApplication.class, args);
	}
}
