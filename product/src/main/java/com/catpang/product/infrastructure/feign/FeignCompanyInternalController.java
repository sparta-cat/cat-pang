package com.catpang.product.infrastructure.feign;

import com.catpang.core.application.dto.CompanyDto;
import com.catpang.core.application.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company-service")
public interface FeignCompanyInternalController {

	@GetMapping("/api/v1/companies/{id}")
	ApiResponse<CompanyDto.Result> getCompany(@PathVariable UUID id);
}
