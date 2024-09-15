package com.catpang.core.infrastructure;

import org.springframework.security.core.userdetails.UserDetails;

import com.catpang.core.domain.model.RoleEnum;

public interface UserRoleChecker {
	RoleEnum getUserRole(UserDetails userDetails);

	boolean isMasterAdmin(UserDetails userDetails);
}
