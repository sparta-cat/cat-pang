package com.catpang.order.presentation;

import static com.catpang.core.application.dto.OrderDto.*;
import static com.catpang.core.domain.model.RoleEnum.Authority.*;
import static com.catpang.order.application.service.OrderMapper.*;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catpang.core.application.dto.OrderProductDto;
import com.catpang.core.infrastructure.UserRoleChecker;
import com.catpang.order.application.service.OrderAuthService;
import com.catpang.order.application.service.OrderService;
import com.catpang.order.domain.repository.OrderSearchCondition;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 주문 관련 API를 제공하는 컨트롤러 클래스입니다.
 *
 * - 새로운 주문 생성, 특정 주문 조회, 주문 목록 조회, 주문 검색, 주문 수정, 주문 삭제 등의 기능을 제공합니다.
 * - 인증된 사용자만 접근할 수 있으며, 일부 기능은 특정 권한을 필요로 합니다.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final OrderAuthService orderAuthService;
	private final UserRoleChecker userRoleChecker;

	/**
	 * 새로운 주문을 생성하는 엔드포인트입니다.
	 * 주문 생성
	 *
	 * @param dto 주문 생성 정보를 담은 DTO 객체
	 * @return 생성된 주문 정보를 반환합니다.
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public Result.With<OrderProductDto.Result> postOrder(Pageable pageable, @Valid @RequestBody Create dto,
		@AuthenticationPrincipal UserDetails userDetails) {
		orderAuthService.requireCompanyOwner(userDetails, dto.orderCompanyId());
		return orderService.createOrder(pageable, dto);
	}

	/**
	 * 특정 주문을 조회하는 엔드포인트입니다.
	 *
	 * @param orderId 조회할 주문의 UUID
	 * @param userDetails 인증된 사용자 정보
	 * @return 조회된 주문 정보를 반환합니다.
	 */
	@PreAuthorize("hasRole('" + HUB_CUSTOMER + "') or hasRole('" + HUB_ADMIN + "') or hasRole('" + MASTER_ADMIN + "')")
	@GetMapping("/{orderId}")
	public Result.Single getOrder(@PathVariable UUID orderId, @AuthenticationPrincipal UserDetails userDetails) {
		orderAuthService.requireOrderOwner(userDetails, orderId);
		return orderService.readOrder(orderId);
	}

	/**
	 * 모든 주문을 페이징하여 조회하는 엔드포인트입니다.
	 *
	 * @param pageable 페이징 정보를 담은 객체
	 * @param userDetails 인증된 사용자 정보
	 * @param id 사용자 ID (선택적)
	 * @return 페이징된 주문 목록을 반환합니다.
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public Page<Result.Single> getOrders(Pageable pageable, @AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(required = false) Long id) {
		boolean isMasterAdmin = userRoleChecker.isMasterAdmin(userDetails);
		return orderService.readOrderAll(pageable, isMasterAdmin, id);
	}

	/**
	 * 검색 조건에 따라 주문을 검색하는 엔드포인트입니다.
	 *
	 * @param ids 검색할 주문의 UUID 리스트 (선택적)
	 * @param ownerIds 주문 소유자 ID 리스트 (선택적)
	 * @param pageable 페이징 정보를 담은 객체
	 * @param userDetails 인증된 사용자 정보
	 * @return 검색된 주문 목록을 반환합니다.
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/search")
	public Page<Result.Single> searchOrder(@RequestParam(required = false) List<UUID> ids,
		@RequestParam(required = false) List<Long> ownerIds, Pageable pageable,
		@AuthenticationPrincipal UserDetails userDetails) {
		boolean isMasterAdmin = userRoleChecker.isMasterAdmin(userDetails);
		if (!isMasterAdmin) {
			ownerIds = List.of(getUserId(userDetails.getUsername()));
		}

		OrderSearchCondition searchCondition = OrderSearchCondition.builder()
			.ids(ids)
			.ownerIds(ownerIds)
			.build();

		return orderService.searchOrder(pageable, searchCondition);
	}

	/**
	 * 특정 주문을 수정하는 엔드포인트입니다.
	 *
	 * @param orderId 수정할 주문의 UUID
	 * @param dto 수정할 주문 정보를 담은 DTO 객체
	 * @param userDetails 인증된 사용자 정보
	 * @return 수정된 주문 정보를 반환합니다.
	 */
	@PreAuthorize("hasRole('" + HUB_ADMIN + "') or hasRole('" + MASTER_ADMIN + "')")
	@PutMapping("/{orderId}")
	public Result.Single putOrder(@PathVariable UUID orderId, @Valid @RequestBody Put dto,
		@AuthenticationPrincipal UserDetails userDetails) {
		orderAuthService.requireOrderOwner(userDetails, orderId);
		return orderService.putOrder(orderId, dto);
	}

	/**
	 * 특정 주문을 소프트 삭제하는 엔드포인트입니다.
	 *
	 * @param orderId 삭제할 주문의 UUID
	 * @param userDetails 인증된 사용자 정보
	 * @return 삭제된 주문 결과를 반환합니다.
	 */
	@PreAuthorize("hasRole('" + HUB_ADMIN + "') or hasRole('" + MASTER_ADMIN + "')")
	@DeleteMapping("/{orderId}")
	public Delete.Result deleteOrder(@PathVariable UUID orderId, @AuthenticationPrincipal UserDetails userDetails) {
		orderAuthService.requireOrderOwner(userDetails, orderId);
		return orderService.softDeleteOrder(orderId);
	}
}