package com.catpang.core.application.service;

import static com.catpang.core.exception.CustomException.*;

public abstract class EntityMapper {

	public static Long getUserId(String stringId) {
		try {
			return Long.parseLong(stringId);
		} catch (NumberFormatException e) {
			throw new InvalidIdFormatException(stringId);
		}
	}
}
