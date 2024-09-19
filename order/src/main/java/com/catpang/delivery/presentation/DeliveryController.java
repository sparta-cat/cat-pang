package com.catpang.delivery.presentation;

import static com.catpang.core.application.dto.DeliveryDto.*;
import static com.catpang.core.domain.model.RoleEnum.Authority.*;
import static com.catpang.delivery.application.service.DeliveryMapper.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catpang.core.infrastructure.UserRoleChecker;
import com.catpang.delivery.application.service.DeliveryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 배송 관련 API를 제공하는 컨트롤러 클래스입니다.
 *
 * - 새로운 배송 생성, 특정 배송 조회, 배송 목록 조회, 배송 검색, 배송 수정, 배송 삭제 등의 기능을 제공합니다.
 * - 인증된 사용자만 접근할 수 있으며, 일부 기능은 특정 권한을 필요로 합니다.
 */
@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService deliveryService;
	private final UserRoleChecker userRoleChecker;

	/**
	 * 배송 조회 및 검색
	 * 모든 로그인 사용자가 조회 및 검색할 수 있으며, 배송 담당자는 자신이 담당하는 배송만 조회 가능
	 *
	 * @param deliveryId 조회할 배송의 UUID
	 * @param userDetails 인증된 사용자 정보
	 * @return 조회된 배송 정보를 반환합니다.
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{deliveryId}")
	public Result getDelivery(@PathVariable UUID deliveryId,
		@AuthenticationPrincipal UserDetails userDetails) {
		boolean isMasterAdmin = userRoleChecker.isMasterAdmin(userDetails);

		// 배송 담당자는 자신이 담당한 배송만 조회 가능
		return deliveryService.readDelivery(deliveryId, isMasterAdmin, getUserId(userDetails.getUsername()));
	}

	/**
	 * 모든 배송을 페이징하여 조회하는 엔드포인트입니다.
	 * 모든 로그인 사용자가 접근 가능, 배송 담당자는 자신이 담당한 배송만 조회 가능
	 *
	 * @param pageable 페이징 정보를 담은 객체
	 * @param userDetails 인증된 사용자 정보
	 * @param ownerId 배송 소유자 ID (선택적)
	 * @return 페이징된 배송 목록을 반환합니다.
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public Page<Result> getDeliveries(Pageable pageable,
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(required = false) Long ownerId) {
		boolean isMasterAdmin = userRoleChecker.isMasterAdmin(userDetails);

		// 배송 담당자는 자신이 담당한 배송만 조회 가능
		return deliveryService.readDeliveryAll(pageable, isMasterAdmin, ownerId);
	}

	/**
	 * 배송을 수정하는 엔드포인트입니다.
	 * 마스터 관리자, 해당 허브 관리자, 또는 해당 배송 담당자만 수정 가능
	 *
	 * @param deliveryId 수정할 배송의 UUID
	 * @param dto 수정할 배송 정보를 담은 DTO 객체
	 * @return 수정된 배송 정보를 반환합니다.
	 */
	@PreAuthorize("hasRole('" + HUB_ADMIN + "') or hasRole('" + MASTER_ADMIN + "') or hasRole('" + DELIVERY + "')")
	@PutMapping("/{deliveryId}")
	public Result putDelivery(@PathVariable UUID deliveryId,
		@Valid @RequestBody Put dto,
		@AuthenticationPrincipal UserDetails userDetails) {

		//TODO: deliveryAuth
		return deliveryService.putDelivery(deliveryId, dto);
	}

	/**
	 * 배송을 삭제하는 엔드포인트입니다.
	 * 마스터 관리자 또는 허브 관리자만 삭제 가능
	 *
	 * @param deliveryId 삭제할 배송의 UUID
	 */
	@PreAuthorize("hasRole('" + MASTER_ADMIN + "') or hasRole('" + HUB_ADMIN + "')")
	@DeleteMapping("/{deliveryId}")
	public void deleteDelivery(@PathVariable UUID deliveryId,
		@AuthenticationPrincipal UserDetails userDetails) {

		//TODO: deliveryAuth
		deliveryService.softDeleteDelivery(deliveryId);
	}
}