package com.catpang.company.domain.repository;

import java.util.UUID;
import org.springframework.stereotype.Component;
import com.catpang.core.domain.repository.helper.AbstractRepositoryHelper;
import com.catpang.company.domain.model.Company;
import jakarta.persistence.EntityManager;

@Component
public class CompanyRepositoryHelper extends AbstractRepositoryHelper<Company, UUID> {

	public CompanyRepositoryHelper(EntityManager em) {
		super(em);
	}
}
