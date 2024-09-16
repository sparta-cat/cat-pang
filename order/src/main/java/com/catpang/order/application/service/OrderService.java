package com.catpang.order.application.service;

import static com.catpang.core.application.dto.OrderDto.*;
import static com.catpang.order.application.service.OrderMapper.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catpang.order.domain.model.Order;
import com.catpang.order.domain.repository.OrderRepository;
import com.catpang.order.domain.repository.OrderRepositoryHelper;
import com.catpang.order.domain.repository.OrderSearchCondition;
import com.catpang.order.infrastructure.feign.FeignCompanyInternalController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderRepositoryHelper repositoryHelper;
	private final FeignCompanyInternalController companyController;

	/**
	 * 새로운 주문을 생성하는 메서드
	 *
	 * @param createDto 주문 생성 정보
	 * @return 생성된 주문 결과
	 */
	@Transactional
	public Result createOrder(Create createDto) {
		UUID companyId = companyController.getCompany(createDto.companyId()).getResult().id();
		Order order = entityFrom(createDto);
		order.setCompanyId(companyId);

		return dtoFrom(orderRepository.save(order));
	}

	/**
	 * 모든 주문을 조회하는 메서드 (관리자 여부에 따라 필터링)
	 *
	 * @param pageable      페이징 정보
	 * @param isMasterAdmin 마스터 관리자 여부
	 * @param ownerId       주문 소유자 ID (관리자가 아닌 경우 필수)
	 * @return 페이징된 주문 결과
	 */
	public Page<Result> readOrderAll(Pageable pageable, boolean isMasterAdmin, Long ownerId) {
		if (isMasterAdmin) {
			return readOrderAll(pageable);
		} else {
			return readOrdersById(pageable, ownerId);
		}
	}

	/**
	 * 주문 ID로 특정 주문을 조회하는 메서드
	 *
	 * @param id 조회할 주문의 ID
	 * @return 조회된 주문 결과
	 */
	public Result readOrder(UUID id) {
		return dtoFrom(repositoryHelper.findOrThrowNotFound(id));
	}

	/**
	 * 모든 주문을 조회하는 내부 메서드 (관리자용)
	 *
	 * @param pageable 페이징 정보
	 * @return 페이징된 주문 결과
	 */
	private Page<Result> readOrderAll(Pageable pageable) {
		return dtoFrom(orderRepository.findAll(pageable));
	}

	/**
	 * 특정 소유자의 주문을 조회하는 내부 메서드
	 *
	 * @param pageable 페이징 정보
	 * @param ownerId  소유자 ID
	 * @return 페이징된 주문 결과
	 */
	private Page<Result> readOrdersById(Pageable pageable, Long ownerId) {
		return dtoFrom(orderRepository.findAllByOwnerId(pageable, ownerId));
	}

	/**
	 * 검색 조건에 따라 주문을 검색하는 메서드
	 *
	 * @param pageable  페이징 정보
	 * @param condition 검색 조건 (주문 ID, 소유자 ID 등을 포함)
	 * @return 페이징된 검색된 주문 결과
	 */
	public Page<Result> searchOrder(Pageable pageable, OrderSearchCondition condition) {
		return dtoFrom(orderRepository.search(condition, pageable));
	}

	/**
	 * 주문을 업데이트하는 메서드
	 *
	 * @param id     업데이트할 주문의 ID
	 * @param putDto 주문 업데이트 정보
	 * @return 업데이트된 주문 결과
	 */
	@Transactional
	public Result putOrder(UUID id, Put putDto) {
		Order order = repositoryHelper.findOrThrowNotFound(id);

		return dtoFrom(orderRepository.save(putToEntity(putDto, order)));
	}

	/**
	 * 주문을 삭제하는 메서드
	 *
	 * @param id 삭제할 주문의 ID
	 */
	@Transactional
	public void deleteOrder(UUID id) {
		Order order = repositoryHelper.findOrThrowNotFound(id);

		orderRepository.delete(order);
	}

	/**
	 * 주문을 소프트 삭제하는 메서드
	 *
	 * @return 소프트 삭제된 주문 결과와 삭제자 정보
	 */
	@Transactional
	public Delete.Result softDeleteOrder(UUID id) {
		Order order = repositoryHelper.findOrThrowNotFound(id);
		order.softDelete();
		return dtoWithDeleter(orderRepository.save(order), order.getDeletedBy());
	}
}