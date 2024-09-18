package com.catpang.hub.presentation;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

/**
 * 허브와 관련된 API 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/v1/hubs")
@RequiredArgsConstructor
@Validated
public class HubController {

	private final HubService hubService;
	private final HubAuthService hubAuthService;

	/**
	 * 새로운 허브를 생성합니다.
	 * @param createDto 허브 생성에 필요한 정보가 담긴 DTO
	 * @param userDetails 인증된 사용자 정보
	 * @return 생성된 허브의 정보가 담긴 DTO
	 */
	@PostMapping
	public HubDto.Result createHub(@Valid @RequestBody HubDto.Create createDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		return hubService.createHub(createDto, userDetails);
	}

	/**
	 * 특정 허브의 정보를 조회합니다.
	 * @param hubId 조회할 허브의 ID
	 * @param userDetails 인증된 사용자 정보
	 * @return 허브의 정보가 담긴 DTO
	 */
	@GetMapping("/{hubId}")
	public HubDto.Result getHub(@PathVariable UUID hubId,
		@AuthenticationPrincipal UserDetails userDetails) {
		// 권한 검증을 수행합니다.
		hubAuthService.requireHubAdmin(userDetails, hubId);
		return hubService.getHub(hubId);
	}

	/**
	 * 허브를 검색 조건에 따라 조회합니다.
	 * @param pageable 페이징 정보
	 * @param condition 검색 조건
	 * @return 허브의 페이징된 결과 DTO
	 */
	@GetMapping
	public Page<HubDto.Result> searchHubs(Pageable pageable, HubSearchCondition condition) {
		return hubService.searchHubs(pageable, condition);
	}

	/**
	 * 기존 허브의 정보를 수정합니다.
	 * @param hubId 수정할 허브의 ID
	 * @param putDto 수정할 내용이 담긴 DTO
	 * @param userDetails 인증된 사용자 정보
	 * @return 수정된 허브의 정보가 담긴 DTO
	 */
	@PutMapping("/{hubId}")
	public HubDto.Result updateHub(@PathVariable UUID hubId,
		@Valid @RequestBody HubDto.Put putDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		// 권한 검증을 수행합니다.
		hubAuthService.requireHubAdmin(userDetails, hubId);
		return hubService.updateHub(hubId, putDto);
	}

	/**
	 * 허브를 삭제합니다.
	 * @param hubId 삭제할 허브의 ID
	 * @param userDetails 인증된 사용자 정보
	 */
	@DeleteMapping("/{hubId}")
	public void deleteHub(@PathVariable UUID hubId,
		@AuthenticationPrincipal UserDetails userDetails) {
		// 권한 검증을 수행합니다.
		hubAuthService.requireHubAdmin(userDetails, hubId);
		hubService.deleteHub(hubId);
	}
}
