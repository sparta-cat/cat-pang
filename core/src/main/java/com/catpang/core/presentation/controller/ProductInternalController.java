package com.catpang.core.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;

import com.catpang.core.application.dto.ProductDto;
import com.catpang.core.application.response.ApiResponse;

public interface ProductInternalController {

	/**
	 * 상품 ID로 특정 회사를 조회하는 메서드
	 *
	 * @param id 조회할 상품의 ID
	 * @return 성공적인 응답과 조회된 상품 정보
	 */

	ApiResponse<ProductDto.Result> getProduct(@PathVariable UUID id);

}