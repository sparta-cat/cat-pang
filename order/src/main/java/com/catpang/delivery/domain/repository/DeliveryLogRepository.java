package com.catpang.delivery.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.catpang.core.domain.repository.BaseRepository;
import com.catpang.delivery.domain.model.DeliveryLog;

@Repository
public interface DeliveryLogRepository extends BaseRepository<DeliveryLog, UUID, DeliveryLogSearchCondition> {
	Page<DeliveryLog> search(DeliveryLogSearchCondition condition, Pageable pageable);

	Page<DeliveryLog> findAllByDeliveryOrderOwnerId(Pageable pageable, Long ownerId);

	DeliveryLog findByIdAndDeliveryOrderOwnerId(UUID id, Long ownerId);
}
