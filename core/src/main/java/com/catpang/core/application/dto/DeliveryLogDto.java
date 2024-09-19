package com.catpang.core.application.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import com.catpang.core.domain.model.DeliveryStatus;

import lombok.Builder;
import lombok.With;

public interface DeliveryLogDto {

	interface Delete {
		@With
		@Builder
		record Result(UUID id, Long deleterId, LocalDateTime deletedAt) {
		}
	}

	@With
	@Builder
	record Create(UUID deliveryId, Integer sequence, DeliveryStatus status, UUID departureHubId,
				  UUID destinationHubId, Double estimatedDistance, Duration estimatedTime) {
	}

	@With
	@Builder
	record Result(UUID id, UUID deliveryId, Integer sequence, DeliveryStatus status, UUID departureHubId,
				  UUID destinationHubId, Double estimatedDistance, Duration estimatedTime,
				  Double actualDistance, Duration actualTime) {
	}
}