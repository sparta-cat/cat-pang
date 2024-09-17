package com.catpang.core.infrastructure;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catpang.core.application.dto.OrderDto;
import com.catpang.core.codes.SuccessCode;

/**
 * 테스트 클래스는 컨트롤러 응답을 GlobalResponseAdvice로 감싸는 것을 확인하는 테스트를 수행합니다.
 */
@WebMvcTest(controllers = {TestController.class, GlobalResponseAdvice.class})
class GlobalResponseAdviceTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * 각 테스트 전에 필요한 초기화를 수행하는 메서드입니다.
	 */
	@BeforeEach
	void setUp() {
		// 필요한 설정이나 초기화 작업
	}

	/**
	 * /test 엔드포인트 호출 시 성공 응답을 확인하는 테스트입니다.
	 * - 상태 코드, 성공 코드, 상태, 구분 코드, 메시지, 그리고 응답 내부의 id 필드를 검증합니다.
	 *
	 * @throws Exception 예외 발생 시 처리
	 */
	@Test
	@WithMockUser(username = "admin", roles = {"MASTER_ADMIN"})
	void ApiResponse_응답_포장_검증() throws Exception {
		mockMvc.perform(get("/test"))
			.andExpect(status().isOk())  // 상태 코드 200 확인
			.andExpect(jsonPath("$.code", is(SuccessCode.SELECT_SUCCESS.name())))  // SuccessCode 확인
			.andExpect(jsonPath("$.status", is("OK")))  // 상태 확인
			.andExpect(jsonPath("$.divisionCode", is("S001")))  // 구분 코드 확인
			.andExpect(jsonPath("$.resultMsg", is("Select success")))  // 메시지 확인
			.andExpect(jsonPath("$.result.id").exists());  // result 내의 id 필드 확인
	}
}

/**
 * TestController는 /test 경로에서 OrderDto.Result를 반환하는 엔드포인트를 제공합니다.
 */
@RestController
class TestController {

	/**
	 * /test 엔드포인트에서 OrderDto.Result 객체를 반환하는 메서드입니다.
	 *
	 * @return OrderDto.Result 객체
	 */
	@GetMapping("/test")
	public OrderDto.Result.Single test() {
		return OrderDto.Result.Single.builder().id(UUID.randomUUID()).build();  // 이 응답이 ApiResponse.Success로 감싸짐
	}
}