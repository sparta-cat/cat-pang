package com.catpang.core.application.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.With;

public interface CompanyDto {

	@With
	@Builder
	record Create(

	) {
	}

	@With
	@Builder
	record Update(

	) {

	}

	@With
	@Builder
	record Result(
		UUID id
	) {
	}
}
