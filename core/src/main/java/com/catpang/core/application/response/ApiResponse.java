package com.catpang.core.application.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.catpang.core.codes.ResponseCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API Response 추상 클래스
 * - 성공 응답 및 에러 응답을 계층화하여 관리
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ApiResponse<T> implements Serializable {
	private T result;                // 결과 데이터 (성공 시 사용)
	private ResponseCode code;       // 성공/에러 코드
	private HttpStatus status;       // HTTP 상태 코드
	private String divisionCode;     // 구분 코드
	private String resultMsg;        // 성공/에러 메시지

	/**
	 * 성공 응답 클래스
	 * @param <T> 응답 데이터의 타입
	 */
	@Getter
	public static class Success<T> extends ApiResponse<T> {

		/**
		 * 성공 응답 생성자
		 * @param result 응답 데이터
		 * @param successCode 성공 코드
		 */
		@Builder
		public Success(T result, ResponseCode successCode) {
			super(result, successCode, successCode.getStatus(), successCode.getDivisionCode(),
				successCode.getMessage());
		}
	}

	/**
	 * 에러 응답 클래스
	 */
	@Getter
	public static class Error extends ApiResponse<Void> {

		private final List<FieldError> errors;  // 필드 에러 리스트
		private final String reason;            // 에러 이유

		/**
		 * 에러 응답 생성자
		 * - 에러 코드와 에러 이유를 기반으로 에러 응답 생성
		 * @param errorCode 에러 코드
		 * @param reason 에러 이유
		 * @param errors 필드 에러 리스트 (null일 경우 빈 리스트로 설정)
		 */
		@Builder
		public Error(ResponseCode errorCode, String reason, List<FieldError> errors) {
			super(null, errorCode, errorCode.getStatus(), errorCode.getDivisionCode(), errorCode.getMessage());
			this.errors = errors != null ? errors : new ArrayList<>();
			this.reason = reason;
		}

		/**
		 * 에러 응답 생성자
		 * - BindingResult를 사용하여 필드 에러를 처리하고 에러 응답 생성
		 * @param errorCode 에러 코드
		 * @param bindingResult 유효성 검증 결과
		 */
		@Builder(builderMethodName = "builderWithBindingResult")
		public Error(ResponseCode errorCode, BindingResult bindingResult) {
			super(null, errorCode, errorCode.getStatus(), errorCode.getDivisionCode(), errorCode.getMessage());
			this.errors = FieldError.of(bindingResult); // 유효성 검증 결과를 통해 필드 에러 리스트 생성
			this.reason = errorCode.getMessage();       // 에러 코드의 메시지를 reason으로 설정
		}
	}

	/**
	 * FieldError 클래스
	 * - 각 필드의 유효성 검사 에러 정보를 담는 클래스
	 * @param field  에러가 발생한 필드명
	 * @param value  잘못된 값
	 * @param reason  에러 이유
	 */
	public record FieldError(String field, String value, String reason) implements Serializable {
		/**
		 * FieldError 리스트 생성
		 * - 단일 필드 에러 정보를 바탕으로 리스트 생성
		 * @param field 에러가 발생한 필드명
		 * @param value 잘못된 값
		 * @param reason 에러 이유
		 * @return FieldError 리스트
		 */
		private static List<FieldError> of(final String field, final String value, final String reason) {
			List<FieldError> fieldErrors = new ArrayList<>();
			fieldErrors.add(new FieldError(field, value, reason));
			return fieldErrors;
		}

		/**
		 * BindingResult로부터 FieldError 리스트 생성
		 * - 유효성 검증 결과를 바탕으로 FieldError 리스트 생성
		 * @param bindingResult 유효성 검증 결과
		 * @return FieldError 리스트
		 */
		private static List<FieldError> of(final BindingResult bindingResult) {
			return bindingResult.getFieldErrors().stream()
				.map(error -> new FieldError(
					error.getField(),
					error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
					error.getDefaultMessage()))
				.toList();
		}
	}
}