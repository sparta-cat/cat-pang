package com.catpang.delivery.domain.repository;

import static com.catpang.delivery.domain.model.QDeliveryLog.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.catpang.core.domain.repository.BaseCustomSearch;
import com.catpang.core.infrastructure.util.PagingUtil;
import com.catpang.delivery.domain.model.DeliveryLog;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliveryLogRepositoryImpl implements BaseCustomSearch<DeliveryLog, DeliveryLogSearchCondition> {

	private final JPAQueryFactory queryFactory;
	private final PagingUtil pagingUtil;

	@Override
	public PageImpl<DeliveryLog> search(DeliveryLogSearchCondition deliveryLogSearchCondition,
		Pageable pageable) {
		JPAQuery<DeliveryLog> query = queryFactory
			.selectFrom(deliveryLog)
			.where(
				inIds(deliveryLogSearchCondition.ids()),
				inOwnerIds(deliveryLogSearchCondition.ownerIds()),
				deliveryLog.delivery.id.in(deliveryLogSearchCondition.deliveryId())
			);

		return pagingUtil.getPageImpl(pageable, query);
	}

	private BooleanExpression inIds(List<UUID> ids) {
		return ids.isEmpty() ? null : deliveryLog.id.in(ids);
	}

	private BooleanExpression inOwnerIds(List<Long> ownerIds) {
		return ownerIds.isEmpty() ? null : deliveryLog.delivery.order.ownerId.in(ownerIds);
	}
}