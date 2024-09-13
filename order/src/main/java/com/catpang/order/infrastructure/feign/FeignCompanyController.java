package com.catpang.order.infrastructure.feign;

import static com.catpang.core.application.dto.CompanyDto.*;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "company-service")
public interface FeignCompanyController {

	@PostMapping("/companies")
	@ResponseStatus(HttpStatus.CREATED)
	Result postCompany(@RequestBody Create dto);

	@GetMapping("/companies/{id}")
	Result getCompany(@PathVariable UUID id);

	@GetMapping("/companies")
	Page<Result> searchCompany();

	@GetMapping("/companies/search")
	Page<Result> searchCompanyByIds(@RequestParam("ids") List<UUID> ids);
}

