package com.catpang.company.presentation.internal;

import com.catpang.company.application.dto.CompanyDto;
import com.catpang.company.application.service.CompanyService;
import com.catpang.core.application.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal/companies")
@RequiredArgsConstructor
public class CompanyInternalController {

	private final CompanyService companyService;

	@GetMapping("/{companyId}")
	public ApiResponse<CompanyDto.Result> getCompanyById(@PathVariable UUID companyId) {
		CompanyDto.Result company = companyService.getCompany(companyId);
		return ApiResponse.Success.<CompanyDto.Result>builder()
			.result(company)
			.build();
	}
}
