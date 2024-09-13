package com.catpang.core.application.service;

import org.springframework.security.core.Authentication;

import com.catpang.core.domain.model.auditing.Timestamped;
import com.catpang.core.domain.repository.helper.RepositoryHelper;

import lombok.AllArgsConstructor;

/**
 * AbstractAuthorizer는 인증된 사용자와 요청자가 동일한지 확인하는 역할을 합니다.
 * 어드민 권한을 가진 사용자는 모든 요청을 통과할 수 있으며,
 * 일반 사용자는 자신의 요청만 처리할 수 있습니다.
 * 이 클래스는 다른 서비스에서 인증 및 권한 확인을 추상적으로 처리할 수 있도록
 * 확장하여 사용할 수 있습니다.
 *
 * @param <U> Timestamped를 확장하는 사용자 엔티티 클래스
 */
@AllArgsConstructor
public abstract class AbstractAuthorizer<U extends Timestamped> {

	protected final AuthenticationInspector authInspector;
	protected final RepositoryHelper<U, Long> repositoryHelper;

	/**
	 * 요청자가 토큰의 유저와 동일한지 확인합니다.
	 * 만약 요청자가 어드민(Admin) 권한을 가진 경우 바로 통과됩니다.
	 *
	 * @param authentication 인증 정보를 포함한 Authentication 객체
	 * @param requester 요청을 보낸 유저 객체
	 * @throws ForbiddenException 요청자가 자신이 아닌 경우 예외 발생 (구현 필요)
	 */
	public void requireByOneself(Authentication authentication, U requester) {
		// 어드민 권한이 있는 경우 통과
		if (authInspector.isAdmin(authentication))
			return;

		// 인증된 유저 정보 가져오기
		U user = (U)authInspector.getUserOrThrow(authentication);

		// 요청자가 토큰의 유저와 다를 경우 예외 발생 (구현 필요)
		if (user != requester) {
			// throw errorHelper.forbidden("Not requested oneself");
		}
	}

	/**
	 * 주어진 ownerId를 기반으로 해당 유저가 요청자와 동일한지 확인합니다.
	 * 해당 유저가 없을 경우 예외가 발생하며, 어드민(Admin) 권한을 가진 경우 바로 통과됩니다.
	 *
	 * @param authentication 인증 정보를 포함한 Authentication 객체
	 * @param ownerId 확인하고자 하는 유저의 ID
	 * @throws ForbiddenException 요청자가 자신이 아닌 경우 또는 유저가 존재하지 않는 경우 예외 발생 (구현 필요)
	 * @throws NotFoundException 주어진 ownerId로 유저를 찾을 수 없는 경우 예외 발생 (구현 필요)
	 */
	public void requireByOneself(Authentication authentication, Long ownerId) {
		// ownerId에 해당하는 유저를 찾고, 없으면 예외 발생
		U owner = repositoryHelper.findOrThrowNotFound(ownerId);

		// owner와 요청자가 동일한지 확인
		requireByOneself(authentication, owner);
	}
}