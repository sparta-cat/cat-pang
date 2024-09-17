package com.catpang.order.application.service;

import static com.catpang.core.application.dto.OrderProductDto.*;

import org.springframework.data.domain.Page;

import com.catpang.order.domain.model.OrderProduct;

public class OrderProductMapper {
	public static OrderProduct entityFrom(Create createDto) {
		return OrderProduct.builder()
			.productId(createDto.productId())
			.quantity(createDto.quantity())
			.build();
	}

	public static Result dtoFrom(OrderProduct orderProduct) {
		return Result.builder()
			.id(orderProduct.getId())
			.productId(orderProduct.getProductId())
			.orderId(orderProduct.getOrder().getId())
			.quantity(orderProduct.getQuantity())
			.build();
	}

	public static Delete.Result dtoWithDeleter(OrderProduct orderProduct, Long deleterId) {
		return Delete.Result.builder()
			.id(orderProduct.getId())
			.deleterId(deleterId)
			.deletedAt(orderProduct.getDeletedAt())
			.build();
	}

	public static Page<Result> dtoFrom(Page<OrderProduct> orderProducts) {
		return orderProducts.map(OrderProductMapper::dtoFrom);
	}

	public static OrderProduct putToEntity(Put putDto, OrderProduct orderProduct) {
		orderProduct.setQuantity(putDto.quantity());
		return orderProduct;
	}
}
