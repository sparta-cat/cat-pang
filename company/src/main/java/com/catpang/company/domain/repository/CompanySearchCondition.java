package com.catpang.company.domain.repository;

import com.catpang.company.domain.model.CompanyType;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

public record CompanySearchCondition(List<UUID> ids, List<CompanyType> types) {

	@Builder
	public CompanySearchCondition {
	}
}
