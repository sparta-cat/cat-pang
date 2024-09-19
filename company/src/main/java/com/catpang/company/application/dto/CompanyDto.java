package com.catpang.company.application.dto;

import java.util.UUID;
import com.catpang.company.domain.model.CompanyType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;

public interface CompanyDto {

	@With
	@Builder
	record Create(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull CompanyType type,
		@NotNull UUID addressId
	) {}

	@With
	@Builder
	record Update(
		@NotNull @Size(min = 1, max = 100) String name,
		@NotNull CompanyType type,
		@NotNull UUID addressId
	) {}

	@With
	@Builder
	record Result(
		UUID id,
		String name,
		CompanyType type,
		UUID addressId
	) {}
}
