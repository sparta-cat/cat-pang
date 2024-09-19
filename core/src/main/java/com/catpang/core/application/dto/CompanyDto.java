package com.catpang.core.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;
import lombok.With;

public interface CompanyDto {

	@With
	@Builder
	record Create(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull String type,  // 업체 타입 (생산/수령 업체)
		@NotNull UUID addressId // 주소 ID
	) {}

	@With
	@Builder
	record Put(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull String type,
		@NotNull UUID addressId
	) {}

	@With
	@Builder
	record Result(
		UUID id,
		String name,
		String type,
		AddressDto.Result address,
    UUID hubId
	) {}
}
