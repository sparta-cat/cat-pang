package com.catpang.hub.domain.repository;

import java.util.UUID;
import org.springframework.stereotype.Component;
import com.catpang.core.domain.repository.helper.AbstractRepositoryHelper;
import com.catpang.hub.domain.model.Hub;
import jakarta.persistence.EntityManager;

@Component
public class HubRepositoryHelper extends AbstractRepositoryHelper<Hub, UUID> {

	public HubRepositoryHelper(EntityManager em) {
		super(em);
	}
}
