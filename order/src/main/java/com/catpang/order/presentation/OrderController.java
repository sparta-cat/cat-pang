package com.catpang.order.presentation;

import static com.catpang.core.application.dto.OrderDto.*;
import static com.catpang.core.codes.SuccessCode.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catpang.core.application.response.ApiResponse.Success;
import com.catpang.order.application.service.OrderService;
import com.catpang.order.domain.repository.OrderSearchCondition;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RequestMapping("/api/v1/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

	private final OrderService service;

	@PostMapping
	public Success<Result> postOrder(@Valid @RequestBody Create dto, UserDetails userDetails) {

		return Success.<Result>builder()
			.result(service.createOrder(dto))
			.successCode(INSERT_SUCCESS)
			.build();
	}

	@GetMapping("/{id}")
	public Success<Result> getOrder(@PathVariable UUID id) {

		return Success.<Result>builder()
			.result(service.readOrder(id))
			.successCode(SELECT_SUCCESS)
			.build();
	}

	@GetMapping
	public Success<Page<Result>> getOrders(Pageable pageable) {

		return Success.<Page<Result>>builder()
			.result(service.readOrderAll(pageable))
			.successCode(SELECT_SUCCESS)
			.build();
	}

	@GetMapping("/search")
	public Success<Page<Result>> searchOrder(
		@RequestParam(required = false) List<UUID> ids,
		@RequestParam(required = false) List<Long> ownerIds,
		Pageable pageable
	) {
		OrderSearchCondition searchCondition = OrderSearchCondition.builder()
			.ids(ids)
			.ownerIds(ownerIds)
			.build();

		return Success.<Page<Result>>builder()
			.result(service.searchOrder(searchCondition, pageable))
			.successCode(SELECT_SUCCESS)
			.build();
	}

	@PutMapping("/{id}")
	public Success<Result> putOrder(@PathVariable UUID id,
		@Valid @RequestBody Put dto, Authentication authentication) {

		return Success.<Result>builder()
			.result(service.putOrder(id, dto))
			.successCode(UPDATE_SUCCESS)
			.build();
	}

	@DeleteMapping("/{id}")
	public Success<Delete.Result> delete(@PathVariable UUID id, Authentication authentication) {

		Long deleterId = -1L;

		return Success.<Delete.Result>builder()
			.result(service.softDeleteOrder(id, deleterId))
			.successCode(DELETE_SUCCESS)
			.build();
	}
}