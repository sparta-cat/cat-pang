package com.catpang.core.application.dto;

import lombok.Builder;

import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

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
	) {}

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
	) {}

	@With
	@Builder
	record Result(
		UUID id,
		String name,
		String city,
		String street,
		String zipcode,
		String latitude,
		String longitude
	) {}
}
