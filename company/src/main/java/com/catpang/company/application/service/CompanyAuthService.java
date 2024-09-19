package com.catpang.company.application.service;

import static com.catpang.core.application.service.EntityMapper.*;

import com.catpang.company.domain.model.Company;
import com.catpang.company.domain.repository.CompanyRepositoryHelper;
import com.catpang.core.infrastructure.UserRoleChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyAuthService {

	private final UserRoleChecker userRoleChecker;
	private final CompanyRepositoryHelper companyRepositoryHelper;

	/**
	 * 사용자에게 회사 관리 권한이 있는지 확인하는 메서드.
	 * - MasterAdmin 역할을 가진 경우 바로 허용.
	 * - 해당 회사의 소유자인 경우 허용.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @param companyId 확인할 회사 ID
	 * @return true: 권한이 있음, false: 권한이 없음
	 */
	public boolean isCompanyAdmin(UserDetails userDetails, UUID companyId) {
		if (userRoleChecker.isMasterAdmin(userDetails)) {
			return true; // MasterAdmin은 모든 권한 허용
		}

		// 회사의 소유자인지 확인
		Company company = companyRepositoryHelper.findOrThrowNotFound(companyId);
		Long ownerId = getUserId(userDetails.getUsername());  // 사용자 ID 확인
		return company.getOwnerId().equals(ownerId);  // 소유자 ID가 사용자 ID와 동일한지 확인
	}
}
