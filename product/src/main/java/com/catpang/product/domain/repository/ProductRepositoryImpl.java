package com.catpang.product.domain.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.catpang.core.domain.repository.BaseCustomSearch;
import com.catpang.core.infrastructure.util.PagingUtil;
import com.catpang.product.domain.model.Product;
import com.catpang.product.domain.model.QProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements BaseCustomSearch<Product, ProductSearchCondition> {

	private final JPAQueryFactory queryFactory;
	private final PagingUtil pagingUtil;

	@Override
	public PageImpl<Product> search(ProductSearchCondition condition, Pageable pageable) {
		JPAQuery<Product> query = queryFactory
			.selectFrom(QProduct.product)
			.where(
				QProduct.product.isDeleted.eq(false),
				nameContains(condition.name()),
				companyIdsIn(condition.companyIds()),
				hubIdsIn(condition.hubIds())
			);

		return pagingUtil.getPageImpl(pageable, query);
	}

	private BooleanExpression nameContains(String name) {
		return name == null || name.isEmpty() ? null : QProduct.product.name.containsIgnoreCase(name);
	}

	private BooleanExpression companyIdsIn(List<UUID> companyIds) {
		return companyIds == null || companyIds.isEmpty() ? null : QProduct.product.companyId.in(companyIds);
	}

	private BooleanExpression hubIdsIn(List<UUID> hubIds) {
		return hubIds == null || hubIds.isEmpty() ? null : QProduct.product.hubId.in(hubIds);
	}
}
