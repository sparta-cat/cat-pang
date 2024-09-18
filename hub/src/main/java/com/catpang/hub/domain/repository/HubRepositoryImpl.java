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

/**
 * 허브 저장소의 구현체로, 커스텀 검색 기능을 제공합니다.
 */
@Component
@RequiredArgsConstructor
public class HubRepositoryImpl implements BaseCustomSearch<Hub, HubSearchCondition> {

	private final JPAQueryFactory queryFactory;
	private final PagingUtil pagingUtil;

	@Override
	public PageImpl<Hub> search(HubSearchCondition condition, Pageable pageable) {
		// 허브를 검색 조건에 따라 조회합니다.
		JPAQuery<Hub> query = queryFactory
			.selectFrom(QHub.hub)
			.where(
				QHub.hub.isDeleted.eq(false), // 삭제되지 않은 허브만 조회
				inIds(condition.ids()),
				inOwnerIds(condition.ownerIds())
			);

		// 페이징된 결과를 반환합니다.
		return pagingUtil.getPageImpl(pageable, query);
	}

	/**
	 * 허브 ID 목록에 해당하는 조건을 생성합니다.
	 * @param ids 허브 ID 목록
	 * @return BooleanExpression 조건
	 */
	private BooleanExpression inIds(List<UUID> ids) {
		return ids.isEmpty() ? null : QHub.hub.id.in(ids);
	}

	/**
	 * 소유자 ID 목록에 해당하는 조건을 생성합니다.
	 * @param ownerIds 소유자 ID 목록
	 * @return BooleanExpression 조건
	 */
	private BooleanExpression inOwnerIds(List<Long> ownerIds) {
		return ownerIds.isEmpty() ? null : QHub.hub.ownerId.in(ownerIds);
	}
}
