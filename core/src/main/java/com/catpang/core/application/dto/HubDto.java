package com.catpang.core.application.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.With;

public interface HubDto {

	@With
	@Builder
	record Result(UUID id, String name, UUID addressId

	) {

	}
}
