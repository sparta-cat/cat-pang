package com.catpang.order.application.service;

import static com.catpang.core.application.dto.OrderDto.*;

import org.springframework.data.domain.Page;

import com.catpang.order.domain.model.Order;

public class OrderMapper {
	public static Order entityFrom(Create createDto) {
		return Order.builder()
			.companyId(createDto.companyId())
			.totalQuantity(createDto.totalQuantity())
			.build();
	}

	public static Result dtoFrom(Order order) {
		return Result.builder()
			.id(order.getId())
			.companyId(order.getCompanyId())
			.totalQuantity(order.getTotalQuantity())
			.build();
	}

	public static Delete.Result dtoWithDeleter(Order order, Long deleterId) {
		return Delete.Result.builder()
			.id(order.getId())
			.deleterId(deleterId)
			.deletedAt(order.getDeletedAt())
			.build();
	}

	public static Page<Result> dtoFrom(Page<Order> orders) {
		return orders.map(OrderMapper::dtoFrom);
	}

	public static Order putToEntity(Put putDto, Order order) {
		order.setTotalQuantity(putDto.totalQuantity());
		return order;
	}
}
