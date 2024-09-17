package com.catpang.order.application.service;

import static com.catpang.core.application.dto.OrderDto.*;
import static com.catpang.order.application.service.OrderMapper.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catpang.core.application.dto.OrderProductDto;
import com.catpang.order.domain.model.Order;
import com.catpang.order.domain.model.OrderProduct;
import com.catpang.order.domain.repository.OrderProductRepository;
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
	private final OrderProductRepository orderProductRepository;
	private final FeignCompanyInternalController companyController;
	private final OrderProductService orderProductService;

	/**
	 * 새로운 주문을 생성하는 메서드
	 * @param pageable 주문상품의 페이징 정보
	 * @param createDto 주문 생성 정보
	 * @return 생성된 주문 결과
	 */
	@Transactional
	public Result.With<OrderProductDto.Result> createOrder(Pageable pageable, Create createDto) {
		UUID companyId = companyController.getCompany(createDto.companyId()).getResult().id();
		Order order = entityFrom(createDto);
		order.setCompanyId(companyId);

		order = orderRepository.save(order);
		Page<OrderProductDto.Result> orderProductResults =
			orderProductService.createOrderProducts(pageable,
				createDto.orderProductDtoes(), order.getId());

		Integer totalQuantity = order.getTotalQuantity();
		totalQuantity += orderProductResults.stream()
			.mapToInt(OrderProductDto.Result::quantity).sum();

		order.setTotalQuantity(totalQuantity);

		return dtoFrom(order, orderProductResults);
	}

	/**
	 * 모든 주문을 조회하는 메서드 (관리자 여부에 따라 필터링)
	 *
	 * @param pageable      페이징 정보
	 * @param isMasterAdmin 마스터 관리자 여부
	 * @param ownerId       주문 소유자 ID (관리자가 아닌 경우 필수)
	 * @return 페이징된 주문 결과
	 */
	public Page<Result.Single> readOrderAll(Pageable pageable, boolean isMasterAdmin, Long ownerId) {
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
	public Result.Single readOrder(UUID id) {
		return dtoFrom(repositoryHelper.findOrThrowNotFound(id));
	}

	/**
	 * 모든 주문을 조회하는 내부 메서드 (관리자용)
	 *
	 * @param pageable 페이징 정보
	 * @return 페이징된 주문 결과
	 */
	private Page<Result.Single> readOrderAll(Pageable pageable) {
		return dtoFrom(orderRepository.findAll(pageable));
	}

	/**
	 * 특정 소유자의 주문을 조회하는 내부 메서드
	 *
	 * @param pageable 페이징 정보
	 * @param ownerId  소유자 ID
	 * @return 페이징된 주문 결과
	 */
	private Page<Result.Single> readOrdersById(Pageable pageable, Long ownerId) {
		return dtoFrom(orderRepository.findAllByOwnerId(pageable, ownerId));
	}

	/**
	 * 검색 조건에 따라 주문을 검색하는 메서드
	 *
	 * @param pageable  페이징 정보
	 * @param condition 검색 조건 (주문 ID, 소유자 ID 등을 포함)
	 * @return 페이징된 검색된 주문 결과
	 */
	public Page<Result.Single> searchOrder(Pageable pageable, OrderSearchCondition condition) {
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
	public Result.Single putOrder(UUID id, Put putDto) {
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
	 * @param orderId 소프트 삭제할 주문의 ID
	 * @return 소프트 삭제된 주문 결과와 삭제자 정보
	 */
	@Transactional
	public Delete.Result softDeleteOrder(UUID orderId) {
		// Order 조회 및 삭제
		Order order = repositoryHelper.findOrThrowNotFound(orderId);
		order.softDelete(); // Order 자체의 soft delete

		// Order에 해당하는 OrderProduct들 조회
		Page<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(Pageable.unpaged(), orderId);

		// OrderProduct들에 soft delete 전파
		for (OrderProduct orderProduct : orderProducts) {
			orderProduct.softDelete();
		}

		// OrderProduct를 배치로 저장
		orderProductRepository.saveAll(orderProducts);

		// 변경된 Order 저장
		return dtoWithDeleter(orderRepository.save(order), order.getDeletedBy());
	}
}