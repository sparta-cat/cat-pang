package com.catpang.product.domain.repository;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

public record ProductSearchCondition(String name, List<UUID> companyIds, List<UUID> hubIds) {

	@Builder
	public ProductSearchCondition {
	}
}
