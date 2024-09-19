package com.catpang.delivery.application.service;

import static com.catpang.core.application.dto.DeliveryDto.*;

import org.springframework.data.domain.Page;

import com.catpang.core.application.service.EntityMapper;
import com.catpang.delivery.domain.model.Delivery;
import com.catpang.order.domain.model.Order;

public class DeliveryMapper extends EntityMapper {

	public static Delivery entityFromWithOrder(Create createDto, Order order) {
		return Delivery.builder()
			.order(order)
			.departureHubId(createDto.departureHubId())
			.destinationHubId(createDto.destinationHubId())
			.receiveCompanyId(createDto.receiveCompanyId())
			.receiverId(createDto.receiverId())
			.receiverSlackId(createDto.receiverSlackId())
			.build();
	}

	public static Result dtoFrom(Delivery delivery) {
		return Result.builder()
			.id(delivery.getId())
			.orderId(delivery.getOrder().getId())
			.departureHubId(delivery.getDepartureHubId())
			.destinationHubId(delivery.getDestinationHubId())
			.status(delivery.getStatus())
			.receiveCompanyId(delivery.getReceiveCompanyId())
			.receiverId(delivery.getReceiverId())
			.receiverSlackId(delivery.getReceiverSlackId())
			.presentAddressId(delivery.getPresentAddressId())
			.build();
	}

	public static Page<Result> dtoFrom(Page<Delivery> deliveries) {
		return deliveries.map(DeliveryMapper::dtoFrom);
	}

	public static Delivery putToEntity(Put putDto, Delivery delivery) {
		delivery.setStatus(putDto.status());
		return delivery;
	}

	public static Delete.Result dtoWithDeleter(Delivery delivery, Long deleterId) {
		return Delete.Result.builder()
			.id(delivery.getId())
			.deleterId(deleterId)
			.deletedAt(delivery.getDeletedAt())
			.build();
	}
}