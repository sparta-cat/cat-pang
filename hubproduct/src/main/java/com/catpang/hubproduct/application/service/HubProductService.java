package com.catpang.hubproduct.application.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.catpang.core.application.dto.HubProductDto;
import com.catpang.hubproduct.domain.model.HubProduct;
import com.catpang.hubproduct.domain.repository.HubProductRepository;
import com.catpang.hubproduct.infrastructure.feign.FeignHubInternalController;
import com.catpang.hubproduct.infrastructure.feign.FeignProductInternalController;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubProductService {

	private final HubProductRepository hubProductRepository;
	private final FeignHubInternalController hubClient;
	private final FeignProductInternalController productClient;

	/**
	 * 허브 상품을 생성합니다.
	 */
	@Transactional
	public HubProductDto.Result createHubProduct(HubProductDto.Create createDto) {
		// 허브 및 상품 존재 여부 검증
		hubClient.getHub(createDto.hubId());
		productClient.getProduct(createDto.productId());

		// HubProduct 생성
		HubProduct hubProduct = HubProduct.builder()
			.hubId(createDto.hubId())
			.productId(createDto.productId())
			.amount(createDto.amount())  // 수량 추가
			.build();

		hubProduct = hubProductRepository.save(hubProduct);
		return toDto(hubProduct);
	}

	/**
	 * 특정 허브 상품을 조회합니다.
	 */
	public HubProductDto.Result getHubProduct(UUID hubProductId) {
		HubProduct hubProduct = hubProductRepository.findById(hubProductId)
			.orElseThrow(() -> new IllegalArgumentException("HubProduct not found"));
		return toDto(hubProduct);
	}

	/**
	 * 허브 상품을 업데이트합니다.
	 */
	@Transactional
	public HubProductDto.Result updateHubProduct(UUID hubProductId, HubProductDto.Update updateDto) {
		// 허브 및 상품 존재 여부 검증
		hubClient.getHub(updateDto.hubId());
		productClient.getProduct(updateDto.productId());

		// HubProduct 조회 후 업데이트
		HubProduct hubProduct = hubProductRepository.findById(hubProductId)
			.orElseThrow(() -> new IllegalArgumentException("HubProduct not found"));

		hubProduct.setHubId(updateDto.hubId());  // 허브 ID 업데이트
		hubProduct.setProductId(updateDto.productId());  // 상품 ID 업데이트
		hubProduct.setAmount(updateDto.amount());  // 수량 업데이트

		return toDto(hubProduct);
	}

	/**
	 * HubProduct 엔티티를 HubProductDto로 변환합니다.
	 */
	private HubProductDto.Result toDto(HubProduct hubProduct) {
		return HubProductDto.Result.builder()
			.id(hubProduct.getId())
			.hubId(hubProduct.getHubId())
			.productId(hubProduct.getProductId())
			.amount(hubProduct.getAmount())  // 수량 추가
			.build();
	}
}
