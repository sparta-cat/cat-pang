package com.catpang.core.exception;

import com.catpang.core.codes.ErrorCode;

import lombok.Getter;

/**
 * [공통 예외 클래스] API에서 발생하는 모든 예외를 관리하는 클래스.
 * 이 클래스는 비즈니스 로직 내에서 발생하는 모든 예외를 처리하며,
 * 각 예외는 {@link ErrorCode}를 통해 구분된다.
 *
 * <p>이 클래스는 기본적으로 {@link RuntimeException}을 확장하여
 * 런타임 예외로 처리되며, 추가적으로 커스텀 메시지를 포함할 수 있다.
 */
@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;
	private final String customMessage;

	/**
	 * 지정된 {@link ErrorCode}와 기본 메시지를 사용하는 생성자.
	 *
	 * @param errorCode 에러 코드 (ErrorCode)
	 */
	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.customMessage = null;
	}

	/**
	 * 지정된 {@link ErrorCode}와 커스텀 메시지를 사용하는 생성자.
	 *
	 * @param errorCode     에러 코드 (ErrorCode)
	 * @param customMessage 커스텀 메시지 (String)
	 */
	public CustomException(ErrorCode errorCode, String customMessage) {
		super(customMessage != null ? customMessage : errorCode.getMessage());
		this.errorCode = errorCode;
		this.customMessage = customMessage;
	}

	/**
	 * 요청자 ID와 사용자 ID 불일치 예외 클래스.
	 * 이 예외는 요청자 ID가 실제 사용자 ID와 일치하지 않을 때 발생한다.
	 */
	@Getter
	public static class UnauthorizedRequesterException extends CustomException {

		/**
		 * 요청자와 사용자 ID가 일치하지 않을 때 발생하는 예외.
		 *
		 * @param requesterId 요청자의 ID
		 * @param userId      실제 사용자 ID
		 */
		public UnauthorizedRequesterException(Long requesterId, Long userId) {
			super(ErrorCode.UNAUTHORIZED,
				String.format("Requester ID (%d) does not match User ID (%d)", requesterId, userId));
		}
	}

	/**
	 * [ID 변환 예외 클래스]
	 * UserDetail의 getUsername 메소드에서 Long ID로 변환할 때 발생하는 예외를 처리.
	 */
	@Getter
	public static class InvalidIdFormatException extends CustomException {

		/**
		 * 문자열을 Long으로 변환할 수 없는 경우 호출되는 예외 생성자.
		 *
		 * @param id 변환할 수 없는 ID 문자열
		 */
		public InvalidIdFormatException(String id) {
			super(ErrorCode.INVALID_TYPE_VALUE, String.format("Invalid ID format: %s", id));
		}
	}
}