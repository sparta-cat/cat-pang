package com.catpang.core.application.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.With;

public interface ProductDto {

	@With
	@Builder
	record Result(UUID id, UUID companyId, String name, Integer price

	) {

	}
}
