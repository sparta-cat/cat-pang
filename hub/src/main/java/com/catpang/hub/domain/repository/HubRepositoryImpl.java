package com.catpang.hub.domain.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.catpang.core.domain.repository.BaseCustomSearch;
import com.catpang.core.infrastructure.util.PagingUtil;
import com.catpang.hub.domain.model.Hub;
import com.catpang.hub.domain.model.QHub;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HubRepositoryImpl implements BaseCustomSearch<Hub, HubSearchCondition> {

	private final JPAQueryFactory queryFactory;
	private final PagingUtil pagingUtil;

	@Override
	public PageImpl<Hub> search(HubSearchCondition condition, Pageable pageable) {
		JPAQuery<Hub> query = queryFactory
			.selectFrom(QHub.hub)
			.where(
				QHub.hub.isDeleted.eq(false),
				inIds(condition.ids()),
				inOwnerIds(condition.ownerIds())
			);

		return pagingUtil.getPageImpl(pageable, query);
	}

	private BooleanExpression inIds(List<UUID> ids) {
		return ids.isEmpty() ? null : QHub.hub.id.in(ids);
	}

	private BooleanExpression inOwnerIds(List<UUID> ownerIds) {
		return ownerIds.isEmpty() ? null : QHub.hub.ownerId.in(ownerIds);
	}
}
