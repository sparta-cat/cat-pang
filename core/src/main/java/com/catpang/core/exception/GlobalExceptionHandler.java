package com.catpang.core.exception;

import static com.catpang.core.application.response.ApiResponse.Error;
import static org.springframework.http.HttpStatus.*;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.catpang.core.codes.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 공통적으로 ErrorResponse 와 ResponseEntity 를 생성하는 메서드
	 * @param errorCode ErrorCode
	 * @param message 에러 메시지
	 * @param bindingResult 유효성 검증 결과 (null 가능)
	 * @return ResponseEntity<Error>
	 */
	private ResponseEntity<Error> buildErrorResponse(ErrorCode errorCode, String message, BindingResult bindingResult) {
		log.error("HttpStatus: {}, Error Code: {}, Message: {}", errorCode.getStatus(), errorCode.getDivisionCode(),
			message);

		Error responseError = bindingResult != null ?
			Error.builderWithBindingResult().errorCode(errorCode).bindingResult(bindingResult).build() :
			Error.builder().errorCode(errorCode).reason(message).build();

		return new ResponseEntity<>(responseError, errorCode.getStatus());
	}

	/**
	 * 공통적인 예외 처리 핸들러
	 * 다양한 예외 유형을 처리하며, 필요에 따라 추가적인 처리 로직을 수행한다.
	 * @param ex 처리할 예외
	 * @return ResponseEntity<Error>
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Error> handleException(Exception ex) {
		// 예외에 따른 ErrorCode 와 추가 처리 로직을 분리된 메서드를 통해 처리
		ExceptionDetails exceptionDetails = getExceptionDetails(ex);
		return buildErrorResponse(exceptionDetails.errorCode, exceptionDetails.message, exceptionDetails.bindingResult);
	}

	/**
	 * 예외를 처리하여 적절한 ErrorCode, 메시지, BindingResult 를 반환하는 메서드
	 * @param ex 처리할 예외
	 * @return ExceptionDetails (ErrorCode, 메시지, BindingResult 정보를 담고 있음)
	 */
	private ExceptionDetails getExceptionDetails(Exception ex) {
		ErrorCode errorCode;
		String message = ex.getMessage();
		BindingResult bindingResult = null;

		// 유효성 검증 실패 예외 처리
		if (ex instanceof MethodArgumentNotValidException) {
			errorCode = ErrorCode.NOT_VALID;
			bindingResult = ((MethodArgumentNotValidException)ex).getBindingResult();

			// 필수 헤더가 누락된 경우 예외 처리
		} else if (ex instanceof MissingRequestHeaderException) {
			errorCode = ErrorCode.NOT_VALID_HEADER;

			// JSON 요청이 잘못된 경우 예외 처리
		} else if (ex instanceof HttpMessageNotReadableException) {
			errorCode = ErrorCode.JSON_PARSE_ERROR;

			// 잘못된 요청 예외 처리
		} else if (ex instanceof HttpClientErrorException.BadRequest) {
			errorCode = ErrorCode.BAD_REQUEST;

			// 잘못된 주소로 요청한 경우 예외 처리
		} else if (ex instanceof NoHandlerFoundException) {
			errorCode = ErrorCode.NOT_FOUND;

			// NULL 값 발생 시 예외 처리
		} else if (ex instanceof NullPointerException) {
			errorCode = ErrorCode.NULL_POINTER;

			// Jackson 처리 오류 발생 시 예외 처리
		} else if (ex instanceof JsonProcessingException) {
			errorCode = ErrorCode.JACKSON_PROCESS_ERROR;

			// 접근 권한이 없는 경우 예외 처리
		} else if (ex instanceof AccessDeniedException) {
			errorCode = ErrorCode.FORBIDDEN;

			// ResponseStatusException 발생 시 예외 처리
		} else if (ex instanceof ResponseStatusException) {
			message = ((ResponseStatusException)ex).getReason();
			errorCode = mapHttpStatusToErrorCode((HttpStatus)((ResponseStatusException)ex).getStatusCode());
			// CustomException 처리
		} else if (ex instanceof CustomException) {
			errorCode = ((CustomException)ex).getErrorCode();

			// 그 외의 모든 예외 처리
		} else {
			errorCode = ErrorCode.INTERNAL_SERVER_ERROR; // 기본적으로 내부 서버 오류 처리
		}

		return new ExceptionDetails(errorCode, message, bindingResult);
	}

	/**
	 * HTTP 상태 코드를 적절한 ErrorCode로 매핑하는 메서드
	 * @param status HTTP 상태 코드
	 * @return 대응하는 ErrorCode
	 */
	private ErrorCode mapHttpStatusToErrorCode(HttpStatus status) {
		if (status == BAD_REQUEST) {
			return ErrorCode.BAD_REQUEST;
		} else if (status == UNAUTHORIZED) {
			return ErrorCode.UNAUTHORIZED;
		} else if (status == FORBIDDEN) {
			return ErrorCode.FORBIDDEN;
		} else if (status == NOT_FOUND) {
			return ErrorCode.NOT_FOUND;
		} else if (status == INTERNAL_SERVER_ERROR) {
			return ErrorCode.INTERNAL_SERVER_ERROR;
		} else {
			return ErrorCode.INTERNAL_SERVER_ERROR; // 기본 처리
		}
	}

	/**
	 * 예외 처리 정보를 담는 클래스
	 */
	private record ExceptionDetails(ErrorCode errorCode, String message, BindingResult bindingResult) {
	}
}