package com.catpang.delivery.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

public record DeliveryLogSearchCondition(List<UUID> ids, List<Long> ownerIds, UUID deliveryId) {

	@Builder
	public DeliveryLogSearchCondition(List<UUID> ids, List<Long> ownerIds, UUID deliveryId) {
		this.ids = ids == null ? new ArrayList<>() : ids;
		this.ownerIds = ownerIds == null ? new ArrayList<>() : ownerIds;
		this.deliveryId = deliveryId;
	}
}
