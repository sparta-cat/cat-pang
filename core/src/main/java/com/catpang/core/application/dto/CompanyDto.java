package com.catpang.core.application.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.With;

public interface CompanyDto {

	@With
	@Builder
	record Result(
		UUID id, Long ownerId, String companyName, String companyAddress, String companyPhone, UUID hubId
	) {
	}
}
