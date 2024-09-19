package com.catpang.delivery.application.service;

import static com.catpang.core.application.dto.DeliveryLogDto.*;
import static com.catpang.delivery.application.service.DeliveryLogMapper.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catpang.delivery.domain.model.Delivery;
import com.catpang.delivery.domain.model.DeliveryLog;
import com.catpang.delivery.domain.repository.DeliveryLogRepository;
import com.catpang.delivery.domain.repository.DeliveryLogRepositoryHelper;
import com.catpang.delivery.domain.repository.DeliveryLogSearchCondition;
import com.catpang.delivery.domain.repository.DeliveryRepositoryHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryLogService {

	private final DeliveryLogRepository deliveryLogRepository;
	private final DeliveryRepositoryHelper deliveryRepositoryHelper;
	private final DeliveryLogRepositoryHelper deliveryLogRepositoryHelper;

	/**
	 * 새로운 배송 로그를 생성하는 메서드
	 *
	 * @param createDto 배송 로그 생성 정보
	 * @return 생성된 배송 로그 결과
	 */
	@Transactional
	public Result createDeliveryLog(Create createDto) {
		Delivery delivery = deliveryRepositoryHelper.findOrThrowNotFound(createDto.deliveryId());
		DeliveryLog deliveryLog = entityFrom(createDto, delivery);

		return dtoFrom(deliveryLogRepository.save(deliveryLog));
	}

	/**
	 * 특정 배송 로그를 조회하는 메서드
	 *
	 * @param id            조회할 배송 로그의 UUID
	 * @param isMasterAdmin
	 * @param ownerId
	 * @return 조회된 배송 로그 결과
	 */
	public Result readDeliveryLog(UUID id, boolean isMasterAdmin, Long ownerId) {
		if (isMasterAdmin) {
			return dtoFrom(deliveryLogRepositoryHelper.findOrThrowNotFound(id));
		}
		return dtoFrom(deliveryLogRepository.findByIdAndDeliveryOrderOwnerId(id, ownerId)); //TODO 예외처리
	}

	/**
	 * 모든 배송 로그를 조회하는 메서드 (관리자 여부에 따라 필터링)
	 *
	 * @param pageable      페이징 정보
	 * @param isMasterAdmin 마스터 관리자 여부
	 * @param ownerId       배송 소유자 ID (관리자가 아닌 경우 필수)
	 * @return 페이징된 배송 로그 결과
	 */
	public Page<Result> readAllDeliveryLogs(Pageable pageable, boolean isMasterAdmin, Long ownerId) {
		if (isMasterAdmin) {
			return dtoFrom(deliveryLogRepository.findAll(pageable));
		} else {
			return dtoFrom(deliveryLogRepository.findAllByDeliveryOrderOwnerId(pageable, ownerId));
		}
	}

	/**
	 * 검색 조건에 따라 배송 로그를 검색하는 메서드
	 *
	 * @param pageable  페이징 정보
	 * @param condition 검색 조건 (배송 ID, 소유자 ID 등을 포함)
	 * @return 페이징된 검색된 배송 결과
	 */
	public Page<Result> searchDelivery(Pageable pageable, DeliveryLogSearchCondition condition) {
		return dtoFrom(deliveryLogRepository.search(condition, pageable));
	}

	/**
	 * 특정 배송 로그를 삭제하는 메서드
	 *
	 * @param id 삭제할 배송 로그의 UUID
	 * @param deleterId 삭제자 ID
	 * @return 삭제된 배송 로그 결과
	 */
	@Transactional
	public Delete.Result softDeleteDeliveryLog(UUID id, Long deleterId) {
		DeliveryLog deliveryLog = deliveryLogRepositoryHelper.findOrThrowNotFound(id);
		deliveryLog.softDelete();
		return dtoWithDeleter(deliveryLog, deleterId);
	}
}