package com.catpang.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
	"spring.cloud.netflix.eureka.enabled=false" // Eureka 클라이언트 비활성화
})
@SpringBootTest
class ConfigApplicationTests {

	@Test
	void contextLoads() {
	}

}