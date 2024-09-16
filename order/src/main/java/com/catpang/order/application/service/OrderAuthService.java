package com.catpang.order.application.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.catpang.core.infrastructure.UserRoleChecker;
import com.catpang.order.domain.repository.OrderRepositoryHelper;
import com.catpang.order.infrastructure.feign.FeignCompanyInternalController;

@Service
public class OrderAuthService extends CompanyAuthService {

	private final OrderRepositoryHelper orderRepositoryHelper;

	/**
	 * OrderAuthService 생성자
	 *
	 * @param companyController     Feign을 통한 회사 서비스 통신 객체
	 * @param userRoleChecker       사용자 역할 확인 객체
	 * @param orderRepositoryHelper 주문 데이터 접근 헬퍼 객체
	 */
	public OrderAuthService(UserRoleChecker userRoleChecker,
		FeignCompanyInternalController companyController,
		OrderRepositoryHelper orderRepositoryHelper) {
		super(userRoleChecker, companyController);
		this.orderRepositoryHelper = orderRepositoryHelper;
	}

	/**
	 * 주문 소유자 여부를 확인하는 메서드입니다.
	 *
	 * @param userDetails      사용자 정보 (Security의 UserDetails 객체)
	 * @param requesterOrderId 주문 ID (UUID 형식)
	 * @throws UnauthorizedRequesterException 사용자가 관리자가 아니며, 주문의 소유자가 아닌 경우 접근이 거부됩니다.
	 * @throws EntityNotFoundException        주문이 존재하지 않을 경우 발생합니다.
	 */
	public void requireOrderOwner(UserDetails userDetails, UUID requesterOrderId) {
		// 관리자인지 확인
		if (userRoleChecker.isMasterAdmin(userDetails)) {
			return;
		}

		// 주문 소유자 확인 (주문이 없을 경우 예외 발생)
		Long requesterId = orderRepositoryHelper.findOrThrowNotFound(requesterOrderId).getOwnerId();

		// 사용자가 해당 주문의 소유자인지 확인 (CompanyAuthService의 메서드 사용)
		requireSelf(userDetails, requesterId);
	}
}