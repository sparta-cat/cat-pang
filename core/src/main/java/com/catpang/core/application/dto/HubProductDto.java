package com.catpang.core.application.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

public interface HubProductDto {

	@With
	@Builder
	record Create(
		@NotNull UUID hubId,
		@NotNull UUID productId,
		@NotNull int amount  // 상품 수량 추가
	) {}

	@With
	@Builder
	record Result(
		UUID id,
		UUID hubId,
		UUID productId,
		int amount  // 상품 수량 추가
	) {}
}
