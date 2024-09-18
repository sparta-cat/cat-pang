package com.catpang.core.application.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

public interface HubDto {

	@With
	@Builder
	record Create(@NotNull String name, @NotNull String address, @NotNull UUID companyId) {
	}

	@With
	@Builder
	record Update(@NotNull String name, @NotNull String address) {
	}

	@With
	@Builder
	record Result(UUID id, String name, String address, UUID companyId) {
	}
}
