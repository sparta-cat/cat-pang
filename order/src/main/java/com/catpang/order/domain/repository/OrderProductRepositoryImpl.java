package com.catpang.order.domain.repository;

import static com.catpang.order.domain.model.QOrderProduct.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.catpang.core.domain.repository.BaseCustomSearch;
import com.catpang.core.infrastructure.util.PagingUtil;
import com.catpang.order.domain.model.OrderProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderProductRepositoryImpl implements BaseCustomSearch<OrderProduct, OrderProductSearchCondition> {

	private final JPAQueryFactory queryFactory;
	private final PagingUtil pagingUtil;

	@Override
	public PageImpl<OrderProduct> search(OrderProductSearchCondition orderProductSearchCondition,
		Pageable pageable) {
		JPAQuery<OrderProduct> query = queryFactory
			.selectFrom(orderProduct)
			.where(
				inIds(orderProductSearchCondition.ids()),
				inOwnerIds(orderProductSearchCondition.ownerIds())
			);

		return pagingUtil.getPageImpl(pageable, query);
	}

	private BooleanExpression inIds(List<UUID> ids) {
		return ids.isEmpty() ? null : orderProduct.id.in(ids);
	}

	private BooleanExpression inOwnerIds(List<Long> ownerIds) {
		return ownerIds.isEmpty() ? null : orderProduct.order.ownerId.in(ownerIds);
	}
}