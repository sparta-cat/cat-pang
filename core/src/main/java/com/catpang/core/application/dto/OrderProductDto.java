package com.catpang.core.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.With;

public interface OrderProductDto {

	interface Delete {
		@With
		@Builder
		record Result(UUID id, Long deleterId, LocalDateTime deletedAt

		) {

		}
	}

	@With
	@Builder
	record Create(UUID productId, Integer quantity) {

	}

	@With
	@Builder
	record Put(Integer quantity) {
	}

	@With
	@Builder
	record Result(UUID id, UUID orderId, UUID productId, Integer quantity

	) {

	}
}
