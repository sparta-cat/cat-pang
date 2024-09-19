package com.catpang.hub.application.dto;

import java.util.UUID;
import com.catpang.core.application.dto.AddressDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;

public interface HubDto {

	@With
	@Builder
	record Create(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull AddressDto.Create address  // Address 생성 정보를 포함
	) {}

	@With
	@Builder
	record Put(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull Long addressId
	) {}

	@With
	@Builder
	record Result(
		UUID id,
		String name,
		AddressDto.Result address,  // Address 객체 반환
		Long ownerId
	) {}
}
