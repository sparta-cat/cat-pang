package com.catpang.order.domain.repository;

import static com.catpang.order.domain.model.QOrder.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.catpang.core.domain.repository.BaseCustomSearch;
import com.catpang.core.infrastructure.util.PagingUtil;
import com.catpang.order.domain.model.Order;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements BaseCustomSearch<Order, OrderSearchCondition> {

	private final JPAQueryFactory queryFactory;
	private final PagingUtil pagingUtil;

	@Override
	public PageImpl<Order> search(OrderSearchCondition orderSearchCondition,
		Pageable pageable) {
		JPAQuery<Order> query = queryFactory
			.selectFrom(order)
			.where(
				inIds(orderSearchCondition.ids()),
				inOwnerIds(orderSearchCondition.ownerIds())
			);

		return pagingUtil.getPageImpl(pageable, query);
	}

	private BooleanExpression inIds(List<UUID> ids) {
		return ids.isEmpty() ? null : order.id.in(ids);
	}

	private BooleanExpression inOwnerIds(List<Long> ownerIds) {
		return ownerIds.isEmpty() ? null : order.ownerId.in(ownerIds);
	}
}