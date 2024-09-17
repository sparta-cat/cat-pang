package com.catpang.order.domain.repository;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.catpang.core.domain.repository.helper.AbstractRepositoryHelper;
import com.catpang.order.domain.model.OrderProduct;

import jakarta.persistence.EntityManager;

/**
 * OrderProduct 엔티티에 대한 RepositoryHelper 구현체.
 */
@Component
public class OrderProductRepositoryHelper extends AbstractRepositoryHelper<OrderProduct, UUID> {

	public OrderProductRepositoryHelper(EntityManager em) {
		super(em);
	}
}