package com.catpang.address.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

import java.util.UUID;



public interface AddressDto {

	@With
	@Builder
	record Create(
		@NotNull String city,
		@NotNull String street,
		String zipcode,
		String latitude,
		String longitude
	) {}

	@With
	@Builder
	record Result(
		UUID id,  // UUID 타입으로 변경
		String city,
		String street,
		String zipcode,
		String latitude,
		String longitude
	) {}
}
