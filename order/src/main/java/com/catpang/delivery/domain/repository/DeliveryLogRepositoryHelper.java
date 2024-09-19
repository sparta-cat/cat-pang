package com.catpang.delivery.domain.repository;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.catpang.core.domain.repository.helper.AbstractRepositoryHelper;
import com.catpang.delivery.domain.model.DeliveryLog;

import jakarta.persistence.EntityManager;

/**
 * DeliveryLog 엔티티에 대한 RepositoryHelper 구현체.
 */
@Component
public class DeliveryLogRepositoryHelper extends AbstractRepositoryHelper<DeliveryLog, UUID> {

	public DeliveryLogRepositoryHelper(EntityManager em) {
		super(em);
	}
}