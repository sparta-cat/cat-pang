package com.catpang.delivery.domain.repository;

import com.catpang.core.domain.repository.BaseCustomSearch;
import com.catpang.core.infrastructure.util.PagingUtil;
import com.catpang.delivery.domain.model.Delivery;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.catpang.delivery.domain.model.QDelivery.*;

@Component
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements BaseCustomSearch<Delivery, DeliverySearchCondition> {

    private final JPAQueryFactory queryFactory;
    private final PagingUtil pagingUtil;

    @Override
    public PageImpl<Delivery> search(DeliverySearchCondition deliverySearchCondition,
                                     Pageable pageable) {
        JPAQuery<Delivery> query = queryFactory
                .selectFrom(delivery)
                .where(
                        inIds(deliverySearchCondition.ids()),
                        inOwnerIds(deliverySearchCondition.ownerIds()),
                        delivery.order.id.in(deliverySearchCondition.orderId())
                );

        return pagingUtil.getPageImpl(pageable, query);
    }

    private BooleanExpression inIds(List<UUID> ids) {
        return ids.isEmpty() ? null : delivery.id.in(ids);
    }

    private BooleanExpression inOwnerIds(List<Long> ownerIds) {
        return ownerIds.isEmpty() ? null : delivery.order.ownerId.in(ownerIds);
    }
}