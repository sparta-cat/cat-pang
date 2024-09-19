package com.catpang.core.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.catpang.core.domain.model.DeliveryStatus;

import lombok.Builder;
import lombok.With;

public interface DeliveryDto {

	interface Delete {
		@With
		@Builder
		record Result(UUID id, Long deleterId, LocalDateTime deletedAt

		) {

		}
	}

	@With
	@Builder
	record Create(UUID orderId, UUID departureHubId, UUID destinationHubId, UUID receiveCompanyId, Long receiverId,
				  UUID receiverSlackId) {

	}

	@With
	@Builder
	record Put(DeliveryStatus status, UUID nextAddressId) {
	}

	@With
	@Builder
	record Result(UUID id, UUID orderId, DeliveryStatus status, UUID departureHubId, UUID destinationHubId,
				  UUID receiveCompanyId, Long receiverId,
				  UUID receiverSlackId, UUID presentAddressId) {

	}
}
