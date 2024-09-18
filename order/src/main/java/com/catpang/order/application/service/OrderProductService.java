package com.catpang.order.application.service;

import static com.catpang.core.application.dto.OrderProductDto.*;
import static com.catpang.order.application.service.OrderProductMapper.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catpang.core.application.dto.ProductDto;
import com.catpang.core.exception.CustomException;
import com.catpang.order.domain.model.Order;
import com.catpang.order.domain.model.OrderProduct;
import com.catpang.order.domain.repository.OrderProductRepository;
import com.catpang.order.domain.repository.OrderProductRepositoryHelper;
import com.catpang.order.domain.repository.OrderProductSearchCondition;
import com.catpang.order.domain.repository.OrderRepositoryHelper;
import com.catpang.order.infrastructure.feign.FeignProductInternalController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class OrderProductService {

	private final OrderProductRepository orderProductRepository;
	private final OrderProductRepositoryHelper orderProductRepositoryHelper;
	private final OrderRepositoryHelper orderRepositoryHelper;
	private final FeignProductInternalController productController;

	/**
	 * 새로운 주문상품을 생성하는 메서드
	 *
	 * @param createDto        주문상품 생성 정보
	 * @param produceCompanyId 주문을 요청할 업체 ID
	 * @return 생성된 주문상품 결과
	 */
	@Transactional
	public Result createOrderProduct(Create createDto, UUID orderId, UUID produceCompanyId) {
		ProductDto.Result result = productController.getProduct(createDto.productId()).getResult();
		if (!result.companyId().equals(produceCompanyId)) {
			throw new CustomException.ProductCompanyMismatchException(produceCompanyId, result.companyId());
		}
		Order order = orderRepositoryHelper.findOrThrowNotFound(orderId);
		OrderProduct orderProduct = entityFrom(createDto);
		orderProduct.setOrder(order);
		orderProduct.setProductId(result.id());

		return dtoFrom(orderProductRepository.save(orderProduct));
	}

	/**
	 * 새로운 주문상품을 생성하는 메서드
	 *
	 * @param createDtoes      주문상품들 생성 정보
	 * @param produceCompanyId
	 * @return 생성된 주문상품 결과
	 */
	@Transactional
	public Page<Result> createOrderProducts(Pageable pageable, List<Create> createDtoes, UUID orderId,
		UUID produceCompanyId) {
		// createDtoes 리스트를 stream으로 열고, 각 createDto에 대해 createOrderProduct 호출
		List<Result> results = createDtoes.stream()
			.map(createDto -> createOrderProduct(createDto, orderId, produceCompanyId)) // 각 Create에 대해 처리
			.collect(Collectors.toList());
		return new PageImpl<>(results, pageable, results.size());
	}

	/**
	 * 모든 주문상품을 조회하는 메서드 (관리자 여부에 따라 필터링)
	 *
	 * @param pageable      페이징 정보
	 * @param isMasterAdmin 마스터 관리자 여부
	 * @param ownerId       주문상품 소유자 ID (관리자가 아닌 경우 필수)
	 * @return 페이징된 주문상품 결과
	 */
	public Page<Result> readOrderProductAll(Pageable pageable, boolean isMasterAdmin, Long ownerId) {
		if (isMasterAdmin) {
			return readOrderProductAll(pageable);
		} else {
			return readOrderProductsByOwnerId(pageable, ownerId);
		}
	}

	/**
	 * 주문상품 ID로 특정 주문상품을 조회하는 메서드
	 *
	 * @param id 조회할 주문상품의 ID
	 * @return 조회된 주문상품 결과
	 */
	public Result readOrderProduct(UUID id) {
		return dtoFrom(orderProductRepositoryHelper.findOrThrowNotFound(id));
	}

	/**
	 * 모든 주문상품을 조회하는 내부 메서드 (관리자용)
	 *
	 * @param pageable 페이징 정보
	 * @return 페이징된 주문상품 결과
	 */
	private Page<Result> readOrderProductAll(Pageable pageable) {
		return dtoFrom(orderProductRepository.findAll(pageable));
	}

	/**
	 * 특정 소유자의 주문상품을 조회하는 내부 메서드
	 *
	 * @param pageable 페이징 정보
	 * @param ownerId  소유자 ID
	 * @return 페이징된 주문상품 결과
	 */
	private Page<Result> readOrderProductsByOwnerId(Pageable pageable, Long ownerId) {
		return dtoFrom(orderProductRepository.findAllByOrderOwnerId(pageable, ownerId));
	}

	/**
	 * 검색 조건에 따라 주문상품을 검색하는 메서드
	 *
	 * @param pageable  페이징 정보
	 * @param condition 검색 조건 (주문상품 ID, 소유자 ID 등을 포함)
	 * @return 페이징된 검색된 주문상품 결과
	 */
	public Page<Result> searchOrderProduct(Pageable pageable, OrderProductSearchCondition condition) {
		return dtoFrom(orderProductRepository.search(condition, pageable));
	}

	/**
	 * 주문상품을 업데이트하는 메서드
	 *
	 * @param id     업데이트할 주문상품의 ID
	 * @param putDto 주문상품 업데이트 정보
	 * @return 업데이트된 주문상품 결과
	 */
	@Transactional
	public Result putOrderProduct(UUID id, Put putDto) {
		OrderProduct orderProduct = orderProductRepositoryHelper.findOrThrowNotFound(id);

		return dtoFrom(orderProductRepository.save(putToEntity(putDto, orderProduct)));
	}

	/**
	 * 주문상품을 삭제하는 메서드
	 *
	 * @param id 삭제할 주문상품의 ID
	 */
	@Transactional
	public void deleteOrderProduct(UUID id) {
		OrderProduct orderProduct = orderProductRepositoryHelper.findOrThrowNotFound(id);

		orderProductRepository.delete(orderProduct);
	}

	/**
	 * 주문상품을 소프트 삭제하는 메서드
	 *
	 * @param id 소프트 삭제할 주문상품의 ID
	 * @return 소프트 삭제된 주문상품 결과와 삭제자 정보
	 */
	@Transactional
	public Delete.Result softDeleteOrderProduct(UUID id) {
		OrderProduct orderProduct = orderProductRepositoryHelper.findOrThrowNotFound(id);
		orderProduct.softDelete();
		return dtoWithDeleter(orderProductRepository.save(orderProduct), orderProduct.getDeletedBy());
	}
}