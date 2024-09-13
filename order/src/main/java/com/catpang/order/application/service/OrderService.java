package com.catpang.order.application.service;

import static com.catpang.core.application.dto.OrderDto.*;
import static com.catpang.order.application.service.OrderMapper.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catpang.order.domain.model.Order;
import com.catpang.order.domain.repository.OrderRepository;
import com.catpang.order.domain.repository.OrderRepositoryHelper;
import com.catpang.order.domain.repository.OrderSearchCondition;
import com.catpang.order.infrastructure.feign.FeignCompanyController;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderRepositoryHelper repositoryHelper;
	private final FeignCompanyController feignCompanyController;

	@Transactional
	public Result createOrder(Create createDto) {
		UUID companyId = feignCompanyController.getCompany(createDto.companyId()).id();
		Order order = entityFrom(createDto);
		order.setCompanyId(companyId);

		return dtoFrom(orderRepository.save(order));
	}

	public Result readOrder(UUID id) {
		return dtoFrom(repositoryHelper.findOrThrowNotFound(id));
	}

	public Page<Result> readOrderAll(Pageable pageable) {
		return dtoFrom(orderRepository.findAll(pageable));
	}

	public Page<Result> searchOrder(OrderSearchCondition condition, Pageable pageable) {
		Page<Order> orders = orderRepository.search(condition, pageable);
		return dtoFrom(orders);
	}

	@Transactional
	public Result putOrder(UUID id, Put putDto) {
		Order order = repositoryHelper.findOrThrowNotFound(id);

		return dtoFrom(orderRepository.save(putToEntity(putDto, order)));
	}

	@Transactional
	public void deleteOrder(UUID id) {
		Order order = repositoryHelper.findOrThrowNotFound(id);

		orderRepository.delete(order);
	}

	@Transactional
	public Delete.Result softDeleteOrder(UUID id, Long deleter) {
		Order order = repositoryHelper.findOrThrowNotFound(id);
		order.setIsDeleted(true);
		order.setDeletedAt(LocalDateTime.now());
		return dtoWithDeleter(orderRepository.save(order), deleter);
	}
}
