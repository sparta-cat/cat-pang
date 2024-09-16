package com.catpang.core.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.catpang.core.application.response.ApiResponse;
import com.catpang.core.codes.SuccessCode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ApiResponse의 직렬화를 테스트하는 클래스입니다.
 */
class ApiResponseTest {

	/**
	 * ApiResponse 객체를 JSON으로 직렬화하는 테스트입니다.
	 *
	 * @throws Exception 직렬화 중 발생할 수 있는 예외
	 */
	@Test
	void apiResponse_직렬화_검증() throws Exception {
		// ObjectMapper를 사용해 JSON 직렬화 테스트
		ObjectMapper objectMapper = new ObjectMapper();
		ApiResponse.Success<String> response = ApiResponse.Success.<String>builder()
			.result("Test data")
			.successCode(SuccessCode.SELECT_SUCCESS) // 적절한 ResponseCode 사용
			.build();

		String json = objectMapper.writeValueAsString(response);
		assertNotNull(json); // 직렬화된 JSON이 null이 아닌지 확인
		System.out.println(json); // 직렬화된 JSON 출력
	}
}