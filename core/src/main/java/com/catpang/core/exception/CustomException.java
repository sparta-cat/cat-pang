package com.catpang.core.exception;

import com.catpang.core.codes.ErrorCode;

import lombok.Getter;

/**
 * [공통 예외 클래스] API에서 발생하는 모든 예외를 관리.
 * 관련된 예외는 Inner Class로 관리.
 */
@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}