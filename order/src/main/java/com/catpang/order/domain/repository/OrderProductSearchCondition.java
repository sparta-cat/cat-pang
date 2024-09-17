package com.catpang.order.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

public record OrderProductSearchCondition(List<UUID> ids, List<Long> ownerIds) {

	@Builder
	public OrderProductSearchCondition(List<UUID> ids, List<Long> ownerIds) {
		this.ids = ids == null ? new ArrayList<>() : ids;
		this.ownerIds = ownerIds == null ? new ArrayList<>() : ownerIds;
	}
}
