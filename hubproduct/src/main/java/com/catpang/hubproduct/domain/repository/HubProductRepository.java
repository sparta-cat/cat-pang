package com.catpang.hubproduct.domain.repository;

import com.catpang.hubproduct.domain.model.HubProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HubProductRepository extends JpaRepository<HubProduct, UUID> {
	List<HubProduct> findByHubId(UUID hubId);  // UUID로 변경
}
