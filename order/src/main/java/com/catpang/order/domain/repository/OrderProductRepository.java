package com.catpang.order.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.catpang.core.domain.repository.BaseRepository;
import com.catpang.order.domain.model.OrderProduct;

@Repository
public interface OrderProductRepository extends BaseRepository<OrderProduct, UUID, OrderProductSearchCondition> {
	Page<OrderProduct> search(OrderProductSearchCondition condition, Pageable pageable);

	Page<OrderProduct> findAllByOrderOwnerId(Pageable pageable, Long ownerId);

	Page<OrderProduct> findAllByOrderId(Pageable pageable, UUID orderId);
}
