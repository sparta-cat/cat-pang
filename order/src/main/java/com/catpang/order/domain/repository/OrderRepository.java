package com.catpang.order.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.catpang.core.domain.repository.BaseRepository;
import com.catpang.order.domain.model.Order;

@Repository
public interface OrderRepository extends BaseRepository<Order, UUID, OrderSearchCondition> {
	Page<Order> search(OrderSearchCondition condition, Pageable pageable);

	Page<Order> findAllByOwnerId(Pageable pageable, Long ownerId);
}
