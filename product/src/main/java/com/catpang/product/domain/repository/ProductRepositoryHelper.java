package com.catpang.product.domain.repository;

import java.util.UUID;
import org.springframework.stereotype.Component;
import com.catpang.core.domain.repository.helper.AbstractRepositoryHelper;
import com.catpang.product.domain.model.Product;
import jakarta.persistence.EntityManager;

@Component
public class ProductRepositoryHelper extends AbstractRepositoryHelper<Product, UUID> {

	public ProductRepositoryHelper(EntityManager em) {
		super(em);
	}
}
