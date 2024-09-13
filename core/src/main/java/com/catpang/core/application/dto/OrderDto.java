package com.catpang.core.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.With;

public interface OrderDto {

	interface Delete {
		@With
		@Builder
		record Result(UUID id, Long deleterId, LocalDateTime deletedAt

		) {

		}
	}

	@With
	@Builder
	record Create(Integer totalQuantity, UUID companyId) {

	}

	@With
	@Builder
	record Put(Integer totalQuantity) {
	}

	@With
	@Builder
	record Result(UUID id, Integer totalQuantity, UUID companyId

	) {

	}
}
