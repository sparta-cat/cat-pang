package com.catpang.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
	"spring.cloud.config.enabled=false",    // Config 서버 비활성화
	"eureka.client.enabled=false"           // Eureka 클라이언트 비활성화
})
class OrderApplicationTests {

	@Test
	void contextLoads() {
	}

}
