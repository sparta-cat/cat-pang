package com.catpang.core.application.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.With;

public interface HubDto {

	@With
	@Builder
	record Create(
		String name,
		String city,
		String street,
		String zipcode,
		String latitude,
		String longitude
	) {
	}

	@With
	@Builder
	record Update(
		UUID id,
		String name,
		String city,
		String street,
		String zipcode,
		String latitude,
		String longitude
	) {
	}

	@With
	@Builder
	record Result(
		UUID id,
		String name,
		String city,
		String street,
		String zipcode,
		String latitude,
		String longitude,
		UUID addressId
	) {
	}
}
