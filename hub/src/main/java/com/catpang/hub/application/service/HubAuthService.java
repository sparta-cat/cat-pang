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

	public HubAuthService(UserRoleChecker userRoleChecker, HubRepositoryHelper hubRepositoryHelper) {
		super(userRoleChecker);
		this.userRoleChecker = userRoleChecker;
		this.hubRepositoryHelper = hubRepositoryHelper;
	}

	public void requireHubAdmin(UserDetails userDetails, UUID hubId) {
		if (userRoleChecker.isMasterAdmin(userDetails)) {
			return;
		}
		UUID ownerId = hubRepositoryHelper.findOrThrowNotFound(hubId).getOwnerId();
		requireSelf(userDetails, ownerId);  // ownerId를 그대로 전달
	}
}
