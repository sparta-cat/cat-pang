package com.catpang.core.application.service;

import static com.catpang.core.application.service.EntityMapper.*;
import static com.catpang.core.exception.CustomException.*;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

import com.catpang.core.infrastructure.UserRoleChecker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * AbstractAuthorizer는 인증된 사용자의 ID와 요청자가 동일한지 확인하는 역할을 합니다.
 *
 * 어드민(Admin) 권한을 가진 사용자는 모든 요청을 처리할 수 있으며,
 * 일반 사용자는 자신의 ID와 요청자의 ID(requesterId)가 일치할 때만 요청을 처리할 수 있습니다.
 *
 * 이 클래스는 userDetails에서 제공하는 사용자 ID와 요청자가 보낸 ID(requesterId)를 비교하여
 * 권한을 확인하는 추상 클래스이며, 다른 서비스에서 확장하여 인증 및 권한 확인 로직을 구현할 수 있습니다.
 *
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractAuthorizer {

	protected final UserRoleChecker userRoleChecker;

	/**
	 * 요청자가 인증된 사용자와 동일한지 확인합니다.
	 * 어드민(Admin) 권한을 가진 경우 요청을 바로 허용하고,
	 * 일반 사용자는 자신의 요청만 처리할 수 있습니다.
	 *
	 * @param userDetails 인증된 사용자 정보를 포함한 UserDetails 객체
	 * @param requesterId 요청을 보낸 사용자의 ID
	 * @throws UnauthorizedRequesterException 요청자가 자신이 아닌 경우 예외 발생
	 */
	public void requireSelf(UserDetails userDetails, Long requesterId) {
		Long userId = getUserId(userDetails.getUsername());
		if ((!requesterId.equals(userId))) {
			throw new UnauthorizedRequesterException(requesterId, userId);
		}
	}
	public void requireSelf(UserDetails userDetails, UUID requesterId) {
		UUID userId = UUID.fromString(userDetails.getUsername());
		if ((!requesterId.equals(userId))) {
			throw new UnauthorizedRequesterException(requesterId, userId);
		}
	}

}