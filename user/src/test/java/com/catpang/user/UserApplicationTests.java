package com.catpang.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
	"spring.cloud.config.enabled=false",    // Config 서버 비활성화
	"eureka.client.enabled=false"           // Eureka 클라이언트 비활성화
})
@SpringBootTest
class UserApplicationTests {

	@Test
	void contextLoads() {
	}

}
