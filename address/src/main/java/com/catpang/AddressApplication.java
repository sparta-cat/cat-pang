package com.catpang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * AddressApplication은 Address 모듈을 실행하는 Spring Boot 애플리케이션입니다.
 */
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.catpang.address", "com.catpang.core"})
public class AddressApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddressApplication.class, args);
	}
}
