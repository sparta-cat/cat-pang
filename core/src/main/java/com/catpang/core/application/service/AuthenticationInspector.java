package com.catpang.core.application.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.catpang.core.domain.model.auditing.Timestamped;

/**
 * AuthenticationInspector는 주어진 인증 정보(Authentication)를 기반으로
 * 유저의 인증 상태를 확인하고, 유저가 어드민 권한을 가지고 있는지 여부를 판단하는 역할을 합니다.
 * 주로 인증된 사용자의 정보나 권한을 확인하는 로직을 중앙에서 관리하기 위한 서비스 클래스입니다.
 *
 * @param <U> 인증된 유저 엔티티 클래스 (Timestamped를 상속받음)
 * @param <D> Spring Security의 UserDetails 인터페이스를 상속받은 클래스
 */
@Component
public class AuthenticationInspector<U extends Timestamped, D extends UserDetails> {

	/**
	 * 인증 토큰을 기반으로 인증된 유저를 반환합니다.
	 * 인증 정보가 없거나 유효하지 않은 경우 예외를 발생시켜야 합니다.
	 * (현재 주석 처리된 부분에서 예외 처리를 구현해야 함)
	 *
	 * @param authentication 인증 정보를 포함한 Authentication 객체
	 * @return U 인증된 유저 객체
	 * @throws UnauthorizedException 인증되지 않은 경우 발생 (구현 필요)
	 * @throws ForbiddenException 유저가 삭제된 경우 발생 (구현 필요)
	 */
	public U getUserOrThrow(Authentication authentication) {
		// if (authentication == null || !authentication.isAuthenticated()) {
		//     // 인증되지 않았을 경우 예외 처리
		// }

		// D userDetails = (D) authentication.getPrincipal();
		// U user = userDetails.;

		// return userRepository.findById(user.getUserId())
		//     .orElseThrow(() -> errorHelper.forbidden("Removed user"));
		return (U)authentication.getPrincipal();
	}

	/**
	 * 인증된 사용자가 어드민 권한을 가지고 있는지 확인합니다.
	 * 인증 정보가 없거나 유효하지 않은 경우 예외를 발생시켜야 합니다.
	 * (현재 주석 처리된 부분에서 예외 처리를 구현해야 함)
	 *
	 * @param authentication 인증 정보를 포함한 Authentication 객체
	 * @return boolean 어드민 권한이 있으면 true, 그렇지 않으면 false
	 * @throws UnauthorizedException 인증되지 않은 경우 발생 (구현 필요)
	 */
	public boolean isAdmin(Authentication authentication) {
		// if (authentication == null || !authentication.isAuthenticated()) {
		//     // 인증되지 않았을 경우 예외 처리
		// }
		// return authentication.getAuthorities().stream()
		//     .anyMatch(authority -> authority.getAuthority().equals(ADMIN));
		return false;
	}
}