package com.catpang.hub.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

public record HubSearchCondition(List<UUID> ids, List<Long> ownerIds) {

	@Builder
	public HubSearchCondition(List<UUID> ids, List<Long> ownerIds) {
		this.ids = ids == null ? new ArrayList<>() : ids;
		this.ownerIds = ownerIds == null ? new ArrayList<>() : ownerIds;
	}
}
