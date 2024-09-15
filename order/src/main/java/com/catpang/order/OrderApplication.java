package com.catpang.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.catpang.order", "com.catpang.core"})
@EnableFeignClients
public class OrderApplication {

	public static void main(String[] args) {

		SpringApplication.run(OrderApplication.class, args);
	}

}
