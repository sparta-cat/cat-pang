package com.catpang.delivery.application.service;

import static com.catpang.core.application.dto.DeliveryLogDto.*;

import org.springframework.data.domain.Page;

import com.catpang.core.application.service.EntityMapper;
import com.catpang.delivery.domain.model.Delivery;
import com.catpang.delivery.domain.model.DeliveryLog;

public class DeliveryLogMapper extends EntityMapper {

	/**
	 * Create DTO를 기반으로 DeliveryLog 엔티티 생성
	 *
	 * @param createDto 배송 로그 생성 DTO
	 * @param delivery  관련 배송 엔티티
	 * @return 생성된 DeliveryLog 엔티티
	 */
	public static DeliveryLog entityFrom(Create createDto, Delivery delivery) {
		return DeliveryLog.builder()
			.delivery(delivery)
			.sequence(createDto.sequence())
			.status(createDto.status())
			.departureHubId(createDto.departureHubId())
			.destinationHubId(createDto.destinationHubId())
			.estimatedDistance(createDto.estimatedDistance())
			.estimatedTime(createDto.estimatedTime())
			.build();
	}

	/**
	 * DeliveryLog 엔티티를 Result DTO로 변환
	 *
	 * @param deliveryLog 배송 로그 엔티티
	 * @return 변환된 Result DTO
	 */
	public static Result dtoFrom(DeliveryLog deliveryLog) {
		return Result.builder()
			.id(deliveryLog.getId())
			.deliveryId(deliveryLog.getDelivery().getId())
			.sequence(deliveryLog.getSequence())
			.status(deliveryLog.getStatus())
			.departureHubId(deliveryLog.getDepartureHubId())
			.destinationHubId(deliveryLog.getDestinationHubId())
			.estimatedDistance(deliveryLog.getEstimatedDistance())
			.estimatedTime(deliveryLog.getEstimatedTime())
			.actualDistance(deliveryLog.getActualDistance())
			.actualTime(deliveryLog.getActualTime())
			.build();
	}

	/**
	 * Page 객체로 전달된 DeliveryLog 엔티티들을 Result DTO로 변환
	 *
	 * @param deliveryLogs 배송 로그 엔티티 Page 객체
	 * @return 변환된 Result DTO Page 객체
	 */
	public static Page<Result> dtoFrom(Page<DeliveryLog> deliveryLogs) {
		return deliveryLogs.map(DeliveryLogMapper::dtoFrom);
	}

	/**
	 * 배송 로그 삭제 결과를 Delete.Result DTO로 변환
	 *
	 * @param deliveryLog 삭제된 배송 로그 엔티티
	 * @param deleterId   삭제자 ID
	 * @return 변환된 Delete.Result DTO
	 */
	public static Delete.Result dtoWithDeleter(DeliveryLog deliveryLog, Long deleterId) {
		return Delete.Result.builder()
			.id(deliveryLog.getId())
			.deleterId(deleterId)
			.deletedAt(deliveryLog.getDeletedAt())
			.build();
	}
}