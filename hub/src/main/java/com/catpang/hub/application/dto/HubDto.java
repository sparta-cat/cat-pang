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
		@NotNull AddressDto.Create address  // Address creation info
	) {}

	@With
	@Builder
	record Put(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull UUID addressId
	) {}

	@With
	@Builder
	record Result(
		UUID id,
		String name,
		AddressDto.Result address,  // Address object returned
		UUID ownerId
	) {}
}
