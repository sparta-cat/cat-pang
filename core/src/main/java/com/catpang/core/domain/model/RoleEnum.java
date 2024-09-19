package com.catpang.core.domain.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleEnum {

	HUB_CUSTOMER(Authority.HUB_CUSTOMER),
	DELIVERY(Authority.DELIVERY),
	HUB_ADMIN(Authority.HUB_ADMIN),
	MASTER_ADMIN(Authority.MASTER_ADMIN);

	private final String authority;

	/**
	 * roleCode에 따른 RoleEnum을 반환
	 *
	 * @param roleCode 전달 받은 권한(Role_권한)형식
	 * @return RoleEnum
	 */
	public static RoleEnum fromRoleCode(String roleCode) {
		for (RoleEnum role : RoleEnum.values()) {
			if (role.getAuthority().equalsIgnoreCase(roleCode)) {
				return role;
			}
		}
		throw new IllegalArgumentException("Invalid role code: " + roleCode);
	}

	public String getAuthority() {
		return this.authority;
	}

	public static class Authority {
		public static final String HUB_CUSTOMER = "ROLE_HUB_CUSTOMER";
		public static final String DELIVERY = "ROLE_DELIVERY";
		public static final String HUB_ADMIN = "ROLE_HUB_ADMIN";
		public static final String MASTER_ADMIN = "ROLE_MASTER_ADMIN";
	}

}

