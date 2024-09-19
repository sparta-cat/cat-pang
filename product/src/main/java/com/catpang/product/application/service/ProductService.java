package com.catpang.product.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catpang.product.application.dto.ProductDto;
import com.catpang.product.domain.model.Product;
import com.catpang.product.domain.repository.ProductRepository;
import com.catpang.product.domain.repository.ProductRepositoryHelper;
import com.catpang.product.domain.repository.ProductSearchCondition;
import com.catpang.product.infrastructure.feign.FeignHubInternalController;
import com.catpang.product.infrastructure.feign.FeignCompanyInternalController;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductRepositoryHelper productRepositoryHelper;
	private final FeignHubInternalController hubClient;
	private final FeignCompanyInternalController companyClient;

	/**
	 * 상품을 생성합니다.
	 *
	 * @param createDto 상품 생성에 필요한 정보가 담긴 DTO
	 * @param userDetails 인증된 사용자 정보
	 * @return 생성된 상품의 정보가 담긴 DTO
	 */
	@Transactional
	public ProductDto.Result createProduct(ProductDto.Create createDto, UserDetails userDetails) {
		// 허브와 회사가 존재하는지 검증
		hubClient.getHub(createDto.hubId());
		companyClient.getCompany(createDto.companyId());

		// Product 생성
		Product product = Product.builder()
			.name(createDto.name())
			.companyId(createDto.companyId())
			.hubId(createDto.hubId())
			.price(createDto.price())
			.build();

		product = productRepository.save(product);
		return toDto(product);
	}

	/**
	 * 특정 상품을 조회합니다.
	 *
	 * @param productId 조회할 상품의 ID
	 * @return 상품의 정보가 담긴 DTO
	 */
	public ProductDto.Result getProduct(UUID productId) {
		Product product = productRepositoryHelper.findOrThrowNotFound(productId);
		return toDto(product);
	}

	/**
	 * 상품을 수정합니다.
	 *
	 * @param productId 수정할 상품의 ID
	 * @param updateDto 수정할 내용이 담긴 DTO
	 * @return 수정된 상품의 정보가 담긴 DTO
	 */
	@Transactional
	public ProductDto.Result updateProduct(UUID productId, ProductDto.Update updateDto) {
		// 상품을 조회합니다.
		Product product = productRepositoryHelper.findOrThrowNotFound(productId);

		// 상품 정보를 업데이트합니다.
		product.update(updateDto);

		// 업데이트된 상품을 저장합니다.
		product = productRepository.save(product);

		// 결과 DTO를 반환합니다.
		return ProductDto.Result.builder()
			.id(product.getId())
			.name(product.getName())
			.companyId(product.getCompanyId())
			.hubId(product.getHubId())
			.price(product.getPrice())
			.build();
	}

	/**
	 * 상품을 검색 조건에 따라 조회합니다.
	 *
	 * @param pageable 페이징 정보
	 * @param condition 검색 조건
	 * @return 상품의 페이징된 결과 DTO
	 */
	public Page<ProductDto.Result> searchProducts(Pageable pageable, ProductSearchCondition condition) {
		return productRepository.search(condition, pageable)
			.map(this::toDto);
	}

	/**
	 * 상품을 삭제합니다. 실제로는 소프트 삭제를 수행합니다.
	 *
	 * @param productId 삭제할 상품의 ID
	 */
	@Transactional
	public void deleteProduct(UUID productId) {
		Product product = productRepositoryHelper.findOrThrowNotFound(productId);
		product.softDelete();
		productRepository.save(product);
	}

	/**
	 * Product 엔티티를 ProductDto로 변환합니다.
	 *
	 * @param product Product 엔티티
	 * @return 변환된 DTO
	 */
	private ProductDto.Result toDto(Product product) {
		return ProductDto.Result.builder()
			.id(product.getId())
			.name(product.getName())
			.companyId(product.getCompanyId())
			.hubId(product.getHubId())
			.price(product.getPrice())
			.build();
	}
}
