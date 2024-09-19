package com.catpang.company.domain.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.catpang.company.domain.model.CompanyType;
import com.catpang.core.domain.repository.BaseCustomSearch;
import com.catpang.core.infrastructure.util.PagingUtil;
import com.catpang.company.domain.model.Company;
import com.catpang.company.domain.model.QCompany;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements BaseCustomSearch<Company, CompanySearchCondition> {

	private final JPAQueryFactory queryFactory;
	private final PagingUtil pagingUtil;

	@Override
	public PageImpl<Company> search(CompanySearchCondition condition, Pageable pageable) {
		JPAQuery<Company> query = queryFactory
			.selectFrom(QCompany.company)
			.where(
				QCompany.company.isDeleted.eq(false),
				inIds(condition.ids()),
				inTypes(condition.types())
			);

		return pagingUtil.getPageImpl(pageable, query);
	}

	private BooleanExpression inIds(List<UUID> ids) {
		return ids.isEmpty() ? null : QCompany.company.id.in(ids);
	}

	private BooleanExpression inTypes(List<CompanyType> types) {
		return types.isEmpty() ? null : QCompany.company.type.in(types);
	}
}
