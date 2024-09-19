package com.catpang.core.application.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

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
