package com.catpang.order.domain.repository;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.catpang.core.domain.repository.helper.AbstractRepositoryHelper;
import com.catpang.order.domain.model.Order;

import jakarta.persistence.EntityManager;

/**
 * Order 엔티티에 대한 RepositoryHelper 구현체.
 */
@Component
public class OrderRepositoryHelper extends AbstractRepositoryHelper<Order, UUID> {

	public OrderRepositoryHelper(EntityManager em) {
		super(em);
	}
}