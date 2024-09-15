package com.catpang.core.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {

	HUB_CUSTOMER(Authority.HUB_CUSTOMER),
	DELIVERY(Authority.DELIVERY),
	HUB_ADMIN(Authority.HUB_ADMIN),
	MASTER_ADMIN(Authority.MASTER_ADMIN),
	;

	private final String authority;

	public static class Authority {
		public static final String HUB_CUSTOMER = "ROLE_HUB_CUSTOMER";
		public static final String DELIVERY = "ROLE_DELIVERY";
		public static final String HUB_ADMIN = "ROLE_HUB_ADMIN";
		public static final String MASTER_ADMIN = "ROLE_MASTER_ADMIN";
	}

}
