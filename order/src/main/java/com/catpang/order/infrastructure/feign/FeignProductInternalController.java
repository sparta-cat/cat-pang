package com.catpang.order.infrastructure.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.catpang.core.application.dto.ProductDto;
import com.catpang.core.application.response.ApiResponse;
import com.catpang.core.presentation.controller.ProductInternalController;

@FeignClient(name = "product-service")
public interface FeignProductInternalController extends ProductInternalController {

	@GetMapping("/api/v1/products/{id}")
	ApiResponse<ProductDto.Result> getProduct(@PathVariable UUID id);
}
