package com.catpang.order.infrastructure.feign;

import com.catpang.core.application.dto.CompanyDto;
import com.catpang.core.application.response.ApiResponse;
import com.catpang.core.presentation.controller.CompanyInternalController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company-service")
public interface FeignCompanyInternalController extends CompanyInternalController {

	@Override
	@GetMapping("/api/v1/internal/companies/{id}")
	ApiResponse<CompanyDto.Result> getCompany(@PathVariable UUID id);
}

