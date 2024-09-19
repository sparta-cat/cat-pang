package com.catpang.product.application.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;

public interface ProductDto {

	@With
	@Builder
	record Create(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull UUID companyId,
		@NotNull UUID hubId,
		int price
	) {}

	@With
	@Builder
	record Update(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull UUID companyId,
		@NotNull UUID hubId,
		int price
	) {}

	@With
	@Builder
	record Result(
		UUID id,
		String name,
		UUID companyId,
		UUID hubId,
		int price
	) {}
}
