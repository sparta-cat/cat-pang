package com.catpang.hub.presentation;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.catpang.hub.application.dto.HubDto;
import com.catpang.hub.application.service.HubAuthService;
import com.catpang.hub.application.service.HubService;
import com.catpang.hub.domain.repository.HubSearchCondition;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hubs")
@RequiredArgsConstructor
@Validated
public class HubController {

	private final HubService hubService;
	private final HubAuthService hubAuthService;

	// 허브 생성: MASTER_ADMIN 또는 허브 관리자만 허용
	@PreAuthorize("hasRole('MASTER_ADMIN') or hasRole('HUB_ADMIN')")
	@PostMapping
	public HubDto.Result createHub(@Valid @RequestBody HubDto.Create createDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		return hubService.createHub(createDto, userDetails);
	}

	// 허브 조회: 허브 관리자 또는 해당 허브 소유자만 허용
	@PreAuthorize("hasRole('MASTER_ADMIN') or @hubAuthService.requireHubAdmin(authentication, #hubId)")
	@GetMapping("/{hubId}")
	public HubDto.Result getHub(@PathVariable UUID hubId,
		@AuthenticationPrincipal UserDetails userDetails) {
		hubAuthService.requireHubAdmin(userDetails, hubId);
		return hubService.getHub(hubId);
	}

	// 허브 목록 검색: 모든 사용자 허용
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public Page<HubDto.Result> searchHubs(Pageable pageable, HubSearchCondition condition) {
		return hubService.searchHubs(pageable, condition);
	}

	// 허브 수정: MASTER_ADMIN 또는 허브 관리자만 허용
	@PreAuthorize("hasRole('MASTER_ADMIN') or @hubAuthService.requireHubAdmin(authentication, #hubId)")
	@PutMapping("/{hubId}")
	public HubDto.Result updateHub(@PathVariable UUID hubId,
		@Valid @RequestBody HubDto.Put putDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		hubAuthService.requireHubAdmin(userDetails, hubId);
		return hubService.updateHub(hubId, putDto);
	}

	// 허브 삭제: MASTER_ADMIN 또는 허브 관리자만 허용
	@PreAuthorize("hasRole('MASTER_ADMIN') or @hubAuthService.requireHubAdmin(authentication, #hubId)")
	@DeleteMapping("/{hubId}")
	public void deleteHub(@PathVariable UUID hubId,
		@AuthenticationPrincipal UserDetails userDetails) {
		hubAuthService.requireHubAdmin(userDetails, hubId);
		hubService.deleteHub(hubId);
	}
}
