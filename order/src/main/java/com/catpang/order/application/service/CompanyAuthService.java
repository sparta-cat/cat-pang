package com.catpang.order.application.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.catpang.core.application.service.AbstractAuthorizer;
import com.catpang.core.infrastructure.UserRoleChecker;
import com.catpang.order.infrastructure.feign.FeignCompanyInternalController;

@Service
public class CompanyAuthService extends AbstractAuthorizer {

	private final FeignCompanyInternalController companyController;

	/**
	 * CompanyAuthService 생성자
	 *
	 * @param userRoleChecker   사용자 역할 확인 객체
	 * @param companyController Feign을 통한 회사 서비스 통신 객체
	 */
	public CompanyAuthService(UserRoleChecker userRoleChecker, FeignCompanyInternalController companyController) {
		super(userRoleChecker);
		this.companyController = companyController;
	}

	/**
	 * 회사 소유자 여부를 확인하는 메서드입니다.
	 *
	 * @param userDetails        사용자 정보 (Security의 UserDetails 객체)
	 * @param requesterCompanyId 회사 ID (UUID 형식)
	 * @throws UnauthorizedRequesterException 사용자가 관리자가 아니며, 회사의 소유자가 아닌 경우 접근이 거부됩니다.
	 */
	public void requireCompanyOwner(UserDetails userDetails, UUID requesterCompanyId) {
		// 관리자인지 확인
		if (userRoleChecker.isMasterAdmin(userDetails)) {
			return;
		}

		// 회사 소유자 확인
		Long requesterId = companyController.getCompany(requesterCompanyId).getResult().ownerId();

		// 사용자가 해당 회사의 소유자인지 확인 (UserAuthService의 메서드 사용)
		requireSelf(userDetails, requesterId);
	}
}