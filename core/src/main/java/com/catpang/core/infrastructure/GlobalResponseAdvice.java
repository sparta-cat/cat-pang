package com.catpang.core.infrastructure;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.catpang.core.application.response.ApiResponse;
import com.catpang.core.codes.SuccessCode;

/**
 * 모든 컨트롤러의 응답을 가로채어 ApiResponse.Success로 감싸는 클래스입니다.
 */
@Component
@RestControllerAdvice(basePackages = "com.catpang")
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

	/**
	 * 어떤 응답을 가공할지 결정하는 메서드입니다.
	 *
	 * @param returnType 반환 타입 정보
	 * @param converterType 메시지 컨버터 타입
	 * @return true를 반환해 모든 응답을 가로챕니다.
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	/**
	 * 실제로 응답을 가공하는 메서드입니다.
	 * ApiResponse.Success로 감싸지 않은 경우 감싸서 반환합니다.
	 *
	 * @param body 응답 본문
	 * @param returnType 반환 타입 정보
	 * @param selectedContentType 선택된 콘텐츠 타입
	 * @param selectedConverterType 메시지 컨버터 타입
	 * @param request 요청 객체
	 * @param response 응답 객체
	 * @return 감싸진 응답 객체
	 */
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType,
		org.springframework.http.server.ServerHttpRequest request,
		org.springframework.http.server.ServerHttpResponse response) {

		// ApiResponse.Success나 ApiResponse.Error로 이미 포장된 경우 그대로 반환
		if (body instanceof ApiResponse) {
			return body;
		}

		// 기본 응답을 ApiResponse.Success로 감싸서 반환
		ApiResponse.Success<Object> successResponse = ApiResponse.Success.builder()
			.result(body)
			.successCode(SuccessCode.SELECT_SUCCESS)
			.build();
		return successResponse;
	}
}