package com.catpang.delivery.application.service;

import static com.catpang.core.application.dto.DeliveryDto.*;
import static com.catpang.core.domain.model.DeliveryStatus.*;
import static com.catpang.delivery.application.service.DeliveryMapper.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catpang.core.application.dto.HubDto;
import com.catpang.core.application.dto.UserDto;
import com.catpang.delivery.domain.model.Delivery;
import com.catpang.delivery.domain.repository.DeliveryRepository;
import com.catpang.delivery.domain.repository.DeliveryRepositoryHelper;
import com.catpang.delivery.domain.repository.DeliverySearchCondition;
import com.catpang.delivery.infrastructure.feign.FeignHubInternalController;
import com.catpang.delivery.infrastructure.feign.FeignUserInternalController;
import com.catpang.order.domain.model.Order;
import com.catpang.order.domain.repository.OrderRepositoryHelper;
import com.catpang.order.infrastructure.feign.FeignCompanyInternalController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final DeliveryRepositoryHelper repositoryHelper;
	private final FeignHubInternalController hubController;
	private final FeignCompanyInternalController companyController;
	private final FeignUserInternalController userController;
	private final OrderRepositoryHelper orderRepositoryHelper;
	private final DeliveryLogService deliveryLogService;

	/**
	 * 새로운 배송을 생성하는 메서드
	 *
	 * @param createDto 배송 생성 정보
	 * @return 생성된 배송 결과
	 */
	@Transactional
	public Result createDelivery(Create createDto) {
		Order order = orderRepositoryHelper.findOrThrowNotFound(createDto.orderId());
		HubDto.Result departureHub = hubController.getHub(createDto.departureHubId()).getResult();
		UUID departureHubId = departureHub.id();
		UUID destinationHubId = hubController.getHub(createDto.destinationHubId()).getResult().id();
		UUID receiveCompanyId = companyController.getCompany(createDto.receiveCompanyId()).getResult().id();
		UserDto.Result owner = userController.getUser(createDto.receiverId()).getResult();
		Long receiverId = owner.id();
		UUID receiverSlackId = UUID.fromString(owner.slackId()); //TODO

		Delivery delivery = DeliveryMapper.entityFromWithOrder(createDto, order);
		delivery.setDepartureHubId(departureHubId);
		delivery.setDestinationHubId(destinationHubId);
		delivery.setReceiveCompanyId(receiveCompanyId);
		delivery.setReceiverId(receiverId);
		delivery.setReceiverSlackId(receiverSlackId);
		delivery.setDestinationSequence(1); //TODO dummy

		delivery = deliveryRepository.save(delivery);
		deliveryLogService.getDeliveryRoute(delivery.getId(), departureHubId, destinationHubId);

		return dtoFrom(delivery);
	}

	/**
	 * 모든 배송을 조회하는 메서드 (관리자 여부에 따라 필터링)
	 *
	 * @param pageable      페이징 정보
	 * @param isMasterAdmin 마스터 관리자 여부
	 * @param ownerId       배송 소유자 ID (관리자가 아닌 경우 필수)
	 * @return 페이징된 배송 결과
	 */
	public Page<Result> readDeliveryAll(Pageable pageable, boolean isMasterAdmin, Long ownerId) {
		if (isMasterAdmin) {
			return readDeliveryAll(pageable);
		} else {
			return readDeliveriesById(pageable, ownerId);
		}
	}

	/**
	 * 배송 ID로 특정 배송을 조회하는 메서드
	 *
	 * @param id 조회할 배송의 ID
	 * @return 조회된 배송 결과
	 */
	public Result readDelivery(UUID id) {
		return dtoFrom(repositoryHelper.findOrThrowNotFound(id));
	}

	/**
	 * 특정 소유자의 배송을 조회하는 내부 메서드
	 *
	 * @param deliveryId 배송 ID
	 * @param ownerId  소유자 ID
	 * @return 페이징된 배송 결과
	 */
	public Result readDelivery(UUID deliveryId, boolean isMasterAdmin, Long ownerId) {
		if (isMasterAdmin) {
			return readDelivery(deliveryId);
		} else {
			return readDeliveryByIdAndOwnerId(deliveryId, ownerId);
		}
	}

	public Result readDeliveryByIdAndOwnerId(UUID deliveryId, Long ownerId) {
		return dtoFrom(deliveryRepository.findByIdAndOrderOwnerId(deliveryId, ownerId));
	}

	/**
	 * 모든 배송을 조회하는 내부 메서드 (관리자용)
	 *
	 * @param pageable 페이징 정보
	 * @return 페이징된 배송 결과
	 */
	private Page<Result> readDeliveryAll(Pageable pageable) {
		return dtoFrom(deliveryRepository.findAll(pageable));
	}

	/**
	 * 특정 소유자의 배송을 조회하는 내부 메서드
	 *
	 * @param pageable 페이징 정보
	 * @param ownerId  소유자 ID
	 * @return 페이징된 배송 결과
	 */
	private Page<Result> readDeliveriesById(Pageable pageable, Long ownerId) {
		return dtoFrom(deliveryRepository.findAllByOrderOwnerId(pageable, ownerId));
	}

	/**
	 * 검색 조건에 따라 배송을 검색하는 메서드
	 *
	 * @param pageable  페이징 정보
	 * @param condition 검색 조건 (배송 ID, 소유자 ID 등을 포함)
	 * @return 페이징된 검색된 배송 결과
	 */
	public Page<Result> searchDelivery(Pageable pageable, DeliverySearchCondition condition) {
		return dtoFrom(deliveryRepository.search(condition, pageable));
	}

	/**
	 * 배송을 업데이트하는 메서드
	 *
	 * @param id     업데이트할 배송의 ID
	 * @param putDto 배송 업데이트 정보
	 * @return 업데이트된 배송 결과
	 */
	@Transactional
	public Result putDelivery(UUID id, Put putDto) {
		Delivery delivery = repositoryHelper.findOrThrowNotFound(id);
		delivery.setStatus(putDto.status());
		delivery.setPresentSequence(putDto.nextSequenceNum());

		return dtoFrom(deliveryRepository.save(putToEntity(putDto, delivery)));
	}

	/**
	 * 배송의 다음 시퀀스를 가져오는 메서드
	 *
	 * @param id 업데이트할 배송의 ID
	 * @return 업데이트된 배송 결과
	 */
	@Transactional
	public Result getNextSequence(UUID id) {
		Delivery delivery = repositoryHelper.findOrThrowNotFound(id);
		return dtoFrom(deliveryRepository.save(nextSequence(delivery)));
	}

	/**
	 * 배송의 시퀀스를 증가시키고 상태를 업데이트하는 메서드
	 *
	 * @param delivery 업데이트할 배송 엔티티
	 * @return 업데이트된 배송 엔티티
	 */
	private Delivery nextSequence(Delivery delivery) {
		Integer pastSequence = delivery.getPresentSequence();
		Integer nextSequence = pastSequence + 1;
		if (delivery.getStatus().equals(OUT_FOR_DELIVERY)) {
			delivery.setStatus(IN_TRANSIT);
		}
		deliveryLogService.setArrived(delivery.getId(), nextSequence);
		delivery.setPresentSequence(nextSequence);

		if (delivery.getDestinationSequence().equals(nextSequence)) {
			delivery.setStatus(ARRIVED);
		}

		return delivery;
	}

	/**
	 * 배송을 삭제하는 메서드
	 *
	 * @param id 삭제할 배송의 ID
	 */
	@Transactional
	public void deleteDelivery(UUID id) {
		Delivery delivery = repositoryHelper.findOrThrowNotFound(id);

		deliveryRepository.delete(delivery);
	}

	/**
	 * 배송을 소프트 삭제하는 메서드
	 *
	 * @param id 소프트 삭제할 배송의 ID
	 * @return 소프트 삭제된 배송 결과와 삭제자 정보
	 */
	@Transactional
	public Delete.Result softDeleteDelivery(UUID id) {
		Delivery delivery = repositoryHelper.findOrThrowNotFound(id);
		delivery.softDelete();
		return dtoWithDeleter(deliveryRepository.save(delivery), delivery.getDeletedBy());
	}
}