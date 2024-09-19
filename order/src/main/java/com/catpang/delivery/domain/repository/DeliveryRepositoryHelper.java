package com.catpang.delivery.domain.repository;

import com.catpang.core.domain.repository.helper.AbstractRepositoryHelper;
import com.catpang.delivery.domain.model.Delivery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Delivery 엔티티에 대한 RepositoryHelper 구현체.
 */
@Component
public class DeliveryRepositoryHelper extends AbstractRepositoryHelper<Delivery, UUID> {

    public DeliveryRepositoryHelper(EntityManager em) {
        super(em);
    }
}