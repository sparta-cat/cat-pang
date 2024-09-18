package com.catpang.hub.application.service;

import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.catpang.core.application.service.AbstractAuthorizer;
import com.catpang.core.infrastructure.UserRoleChecker;
import com.catpang.hub.domain.repository.HubRepositoryHelper;
import lombok.RequiredArgsConstructor;

@Service
public class HubAuthService extends AbstractAuthorizer {

	private final UserRoleChecker userRoleChecker;
	private final HubRepositoryHelper hubRepositoryHelper;

	// 생성자 명시
	public HubAuthService(UserRoleChecker userRoleChecker, HubRepositoryHelper hubRepositoryHelper) {
		super(userRoleChecker);  // 부모 클래스 생성자 호출
		this.userRoleChecker = userRoleChecker;
		this.hubRepositoryHelper = hubRepositoryHelper;
	}

	public void requireHubAdmin(UserDetails userDetails, UUID hubId) {
		if (userRoleChecker.isMasterAdmin(userDetails)) {
			return;
		}
		Long ownerId = hubRepositoryHelper.findOrThrowNotFound(hubId).getOwnerId();
		requireSelf(userDetails, ownerId);
	}
}
