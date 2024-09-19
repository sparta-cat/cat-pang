package com.catpang.company.presentation;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.catpang.company.application.dto.CompanyDto;
import com.catpang.company.application.service.CompanyService;
import com.catpang.company.domain.repository.CompanySearchCondition;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Validated
public class CompanyController {

	private final CompanyService companyService;

	@PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
	@PostMapping
	public CompanyDto.Result createCompany(@Valid @RequestBody CompanyDto.Create createDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		return companyService.createCompany(createDto, userDetails);
	}

	@PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
	@GetMapping("/{companyId}")
	public CompanyDto.Result getCompany(@PathVariable UUID companyId) {
		return companyService.getCompany(companyId);
	}

	@PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
	@PutMapping("/{companyId}")
	public CompanyDto.Result updateCompany(@PathVariable UUID companyId,
		@Valid @RequestBody CompanyDto.Update updateDto) {
		return companyService.updateCompany(companyId, updateDto);
	}

	@PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
	@GetMapping
	public Page<CompanyDto.Result> searchCompanies(Pageable pageable, CompanySearchCondition condition) {
		return companyService.searchCompanies(pageable, condition);
	}

	@PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
	@DeleteMapping("/{companyId}")
	public void deleteCompany(@PathVariable UUID companyId) {
		companyService.deleteCompany(companyId);
	}
}
