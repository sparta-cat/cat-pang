package com.catpang.delivery.presentation;

import static com.catpang.core.application.dto.DeliveryLogDto.*;
import static com.catpang.core.application.service.EntityMapper.*;
import static com.catpang.core.domain.model.RoleEnum.Authority.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catpang.core.infrastructure.UserRoleChecker;
import com.catpang.delivery.application.service.DeliveryLogService;
import com.catpang.delivery.domain.repository.DeliveryLogSearchCondition;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/delivery-logs")
@RequiredArgsConstructor
public class DeliveryLogController {

	private final DeliveryLogService deliveryLogService;
	private final UserRoleChecker userRoleChecker;

	/**
	 * 특정 배송 로그를 조회하는 엔드포인트입니다.
	 *
	 * @param deliveryLogId 조회할 배송 로그의 UUID
	 * @param userDetails 인증된 사용자 정보
	 * @return 조회된 배송 로그 정보를 반환합니다.
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{deliveryLogId}")
	public Result getDeliveryLog(@PathVariable UUID deliveryLogId,
		@AuthenticationPrincipal UserDetails userDetails) {
		boolean isMasterAdmin = userRoleChecker.isMasterAdmin(userDetails);
		Long ownerId = getUserId(userDetails.getUsername());

		return deliveryLogService.readDeliveryLog(deliveryLogId, isMasterAdmin, ownerId);
	}

	/**
	 * 모든 배송 로그를 조회하는 엔드포인트입니다.
	 *
	 * @param pageable 페이징 정보를 담은 객체
	 * @param userDetails 인증된 사용자 정보
	 * @return 페이징된 배송 로그 목록을 반환합니다.
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public Page<Result> getAllDeliveryLogs(Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
		boolean isMasterAdmin = userRoleChecker.isMasterAdmin(userDetails);
		Long ownerId = getUserId(userDetails.getUsername());

		return deliveryLogService.readAllDeliveryLogs(pageable, isMasterAdmin, ownerId);
	}

	/**
	 * 검색 조건에 따라 배송 로그를 검색하는 엔드포인트입니다.
	 *
	 * @param ids 검색할 배송 로그의 UUID 리스트 (선택적)
	 * @param ownerIds 배송 소유자 ID 리스트 (선택적)
	 * @param deliveryId 배송 ID (선택적)
	 * @param pageable 페이징 정보를 담은 객체
	 * @param userDetails 인증된 사용자 정보
	 * @return 검색된 배송 로그 목록을 반환합니다.
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/search")
	public Page<Result> searchDeliveryLog(@RequestParam(required = false) List<UUID> ids,
		@RequestParam(required = false) List<Long> ownerIds,
		@RequestParam(required = false) UUID deliveryId,
		Pageable pageable,
		@AuthenticationPrincipal UserDetails userDetails) {
		boolean isMasterAdmin = userRoleChecker.isMasterAdmin(userDetails);
		if (!isMasterAdmin) {
			ownerIds = List.of(getUserId(userDetails.getUsername()));
		}

		DeliveryLogSearchCondition searchCondition = DeliveryLogSearchCondition.builder()
			.ids(ids)
			.ownerIds(ownerIds)
			.deliveryId(deliveryId)
			.build();

		return deliveryLogService.searchDelivery(pageable, searchCondition);
	}

	/**
	 * 특정 배송 로그를 삭제하는 엔드포인트입니다.
	 *
	 * @param deliveryLogId 삭제할 배송 로그의 UUID
	 * @param userDetails 인증된 사용자 정보
	 * @return 삭제된 배송 로그 결과를 반환합니다.
	 */
	@PreAuthorize("hasRole('" + MASTER_ADMIN + "')")
	@DeleteMapping("/{deliveryLogId}")
	public Delete.Result deleteDeliveryLog(@PathVariable UUID deliveryLogId,
		@AuthenticationPrincipal UserDetails userDetails) {
		return deliveryLogService.softDeleteDeliveryLog(deliveryLogId, getUserId(userDetails.getUsername()));
	}
}