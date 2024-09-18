package com.catpang.hub.infrastructure.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.catpang.core.application.dto.CompanyDto;
import com.catpang.core.application.response.ApiResponse;
import com.catpang.core.presentation.controller.CompanyInternalController;

@FeignClient(name = "company-service")
public interface FeignCompanyInternalController extends CompanyInternalController {

	@Override
	@GetMapping("/companies/{id}")
	ApiResponse<CompanyDto.Result> getCompany(@PathVariable UUID id);
}
