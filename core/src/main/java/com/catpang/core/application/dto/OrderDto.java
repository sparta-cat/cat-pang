package com.catpang.core.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import jakarta.validation.constraints.NotEmpty;
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

	interface Result {
		@lombok.With
		@Builder
		record Single(UUID id, Integer totalQuantity, UUID orderCompanyId, Long ownerId

		) {

		}

		@Builder
		record With<R>(UUID id, Integer totalQuantity, UUID orderCompanyId, Long ownerId,
					   Page<R> results) {

		}
	}

	@With
	@Builder
	record Put(@PositiveOrZero Integer totalQuantity) {
	}

	@With
	@Builder
	record Create(@NotNull UUID orderCompanyId, @NotNull Long ownerId,
				  @NotEmpty List<OrderProductDto.Create> orderProductDtoes) {

	}

}
