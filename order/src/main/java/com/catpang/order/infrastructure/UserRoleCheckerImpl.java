package com.catpang.order.infrastructure;

import static com.catpang.core.domain.model.RoleEnum.Authority.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.catpang.core.domain.model.RoleEnum;
import com.catpang.core.infrastructure.UserRoleChecker;

/**
 * 사용자의 권한(RoleEnum)을 가져오는 서비스 클래스.
 */
@Service
public class UserRoleCheckerImpl implements UserRoleChecker {

	/**
	 * 인증 정보(Authentication)에서 사용자의 권한(RoleEnum)을 반환합니다.
	 * 사용자는 RoleEnum 중 하나의 권한만 가집니다.
	 *
	 * @param authentication 인증 정보를 포함한 Authentication 객체
	 * @return RoleEnum 사용자가 가진 권한
	 * @throws IllegalArgumentException 권한이 유효하지 않을 경우 발생
	 */
	public RoleEnum getUserRole(UserDetails userDetails) {

		// 권한 리스트에서 첫 번째 권한을 가져옴 (한 개의 권한만 가지므로)
		GrantedAuthority authority = userDetails.getAuthorities().stream()
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("사용자가 권한을 가지고 있지 않습니다."));

		// 권한 문자열을 RoleEnum으로 변환
		return mapAuthorityToRoleEnum(authority.getAuthority());
	}

	@Override
	public boolean isMasterAdmin(UserDetails userDetails) {
		// userDetails에서 권한 정보를 가져와서 'ROLE_MASTER_ADMIN'을 포함하는지 확인
		return userDetails.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)  // 권한 이름 가져오기
			.anyMatch(role -> role.equals(MASTER_ADMIN));  // 'ROLE_MASTER_ADMIN' 비교
	}

	/**
	 * 권한 문자열을 RoleEnum으로 매핑합니다.
	 *
	 * @param authority 권한 문자열 (예: "ROLE_HUB_CUSTOMER")
	 * @return RoleEnum 권한 문자열에 해당하는 RoleEnum
	 * @throws IllegalArgumentException 권한이 RoleEnum에 없을 경우 발생
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