package com.catpang.company.infrastructure;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.catpang.core.domain.model.RoleEnum;
import com.catpang.core.infrastructure.UserRoleChecker;

/**
 * 사용자 역할을 확인하는 구현체입니다.
 */
@Service
public class UserRoleCheckerImpl implements UserRoleChecker {

	/**
	 * 사용자 권한을 기반으로 RoleEnum을 반환합니다.
	 * @param userDetails 사용자 정보
	 * @return 사용자의 RoleEnum
	 */
	@Override
	public RoleEnum getUserRole(UserDetails userDetails) {
		// 사용자의 권한 목록에서 RoleEnum을 찾습니다.
		return userDetails.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.map(this::mapAuthorityToRoleEnum)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("사용자에게 유효한 권한이 없습니다."));
	}

	/**
	 * 사용자가 마스터 관리자 권한을 가지고 있는지 확인합니다.
	 * @param userDetails 사용자 정보
	 * @return 마스터 관리자 권한 여부
	 */
	@Override
	public boolean isMasterAdmin(UserDetails userDetails) {
		// 사용자의 권한 목록에서 마스터 관리자 권한을 찾습니다.
		return userDetails.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(authority -> RoleEnum.MASTER_ADMIN.getAuthority().equals(authority));
	}

	/**
	 * 권한 문자열을 RoleEnum으로 매핑합니다.
	 * @param authority 권한 문자열
	 * @return 매핑된 RoleEnum
	 * @throws IllegalArgumentException 유효하지 않은 권한인 경우 예외 발생
	 */
	private RoleEnum mapAuthorityToRoleEnum(String authority) {
		for (RoleEnum roleEnum : RoleEnum.values()) {
			if (roleEnum.getAuthority().equals(authority)) {
				return roleEnum;
			}
		}
		throw new IllegalArgumentException("유효하지 않은 권한: " + authority);
	}
}
