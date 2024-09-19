package com.catpang.core.application.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.With;

public interface AddressDto {

	@With
	@Builder
	record Result(UUID id, String state, String detail, Double latitude, Double longitude

	) {

	}
}
