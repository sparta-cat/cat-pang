package com.catpang.order.presentation.internal;

import static com.catpang.core.application.dto.OrderDto.*;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catpang.order.application.service.OrderService;

import lombok.RequiredArgsConstructor;

@Validated
@RequestMapping("/api/v1/internal/orders")
@RestController
@RequiredArgsConstructor
class OrderInternalControllerImpl {

	private final OrderService orderService;

	/**
	 * 주문 ID로 특정 주문을 조회하는 메서드
	 *
	 * @param orderId 조회할 주문의 ID
	 * @return 성공적인 응답과 조회된 주문 정보
	 */
	@GetMapping("/{orderId}")
	public Result.Single getOrder(@PathVariable UUID orderId) {
		return orderService.readOrder(orderId);
	}
}