package com.catpang.order.application.service;

import static com.catpang.core.application.dto.OrderDto.*;

import org.springframework.data.domain.Page;

import com.catpang.core.application.service.EntityMapper;
import com.catpang.order.domain.model.Order;

public class OrderMapper extends EntityMapper {
	public static Order entityFrom(Create createDto) {
		return Order.builder()
			.companyId(createDto.companyId())
			.ownerId(createDto.ownerId())
			.build();
	}

	public static Result.Single dtoFrom(Order order) {
		return Result.Single.builder()
			.id(order.getId())
			.companyId(order.getCompanyId())
			.totalQuantity(order.getTotalQuantity())
			.ownerId(order.getOwnerId())
			.build();
	}

	public static Delete.Result dtoWithDeleter(Order order, Long deleterId) {
		return Delete.Result.builder()
			.id(order.getId())
			.deleterId(deleterId)
			.deletedAt(order.getDeletedAt())
			.build();
	}

	public static Page<Result.Single> dtoFrom(Page<Order> orders) {
		return orders.map(OrderMapper::dtoFrom);
	}

	public static Order putToEntity(Put putDto, Order order) {
		order.setTotalQuantity(putDto.totalQuantity());
		return order;
	}
}
