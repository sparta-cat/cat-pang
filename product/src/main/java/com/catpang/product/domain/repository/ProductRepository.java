package com.catpang.product.domain.repository;

import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.catpang.core.domain.repository.BaseRepository;
import com.catpang.product.domain.model.Product;

@Repository
public interface ProductRepository extends BaseRepository<Product, UUID, ProductSearchCondition> {
}
