package com.catpang.hub.application.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;

/**
 * 허브 관련 데이터 전송 객체(DTO)를 정의하는 인터페이스입니다.
 */
public interface HubDto {

	/**
	 * 허브 생성 요청을 위한 DTO입니다.
	 */
	@With
	@Builder
	record Create(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull @Size(min = 1, max = 200) String address
	) {}

	/**
	 * 허브 수정 요청을 위한 DTO입니다.
	 */
	@With
	@Builder
	record Put(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull @Size(min = 1, max = 200) String address
	) {}

	/**
	 * 허브 정보를 반환하기 위한 DTO입니다.
	 */
	@With
	@Builder
	record Result(
		UUID id,
		String name,
		String address,
		Long ownerId
	) {}
}
