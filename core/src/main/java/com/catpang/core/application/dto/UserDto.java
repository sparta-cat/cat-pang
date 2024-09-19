package com.catpang.core.application.dto;

import lombok.Builder;
import lombok.With;

public interface UserDto {

	@With
	@Builder
	record Result(
		Long id,
		String name,
		String nickname,
		String email,
		String role,
		String slackId
	) {
	}
}

