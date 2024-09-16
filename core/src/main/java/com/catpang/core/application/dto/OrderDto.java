package com.catpang.core.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
	record Create(@NotNull @PositiveOrZero Integer totalQuantity, @NotNull UUID companyId, @NotNull Long ownerId) {

	}

	@With
	@Builder
	record Put(@PositiveOrZero Integer totalQuantity) {
	}

	@With
	@Builder
	record Result(UUID id, Integer totalQuantity, UUID companyId, Long ownerId

	) {

	}
}
