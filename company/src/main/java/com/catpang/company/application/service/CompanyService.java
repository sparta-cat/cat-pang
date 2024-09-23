package com.catpang.company.application.service;

import com.catpang.company.application.dto.CompanyDto;
import com.catpang.company.domain.model.Company;
import com.catpang.company.domain.repository.CompanyRepository;
import com.catpang.company.domain.repository.CompanyRepositoryHelper;
import com.catpang.company.domain.repository.CompanySearchCondition;
import com.catpang.company.infrastructure.feign.FeignAddressClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

	private final CompanyRepository companyRepository;
	private final CompanyRepositoryHelper companyRepositoryHelper;
	private final FeignAddressClient addressClient;

	@Transactional
	public CompanyDto.Result createCompany(CompanyDto.Create createDto, UserDetails userDetails) {
		System.out.println("asdlfsadfsdfasdsadfsd");
		addressClient.getAddress(createDto.addressId());

		Company company = Company.builder()
			.name(createDto.name())
			.type(createDto.type())
			.addressId(createDto.addressId())
			.ownerId(UUID.fromString(userDetails.getUsername()))
			.build();

		company = companyRepository.save(company);
		return toDto(company);
	}

	public CompanyDto.Result getCompany(UUID companyId) {
		Company company = companyRepositoryHelper.findOrThrowNotFound(companyId);
		return toDto(company);
	}

	@Transactional
	public CompanyDto.Result updateCompany(UUID companyId, CompanyDto.Update updateDto) {
		Company company = companyRepositoryHelper.findOrThrowNotFound(companyId);
		company.update(updateDto.name(), updateDto.type(), updateDto.addressId());
		return toDto(company);
	}

	public Page<CompanyDto.Result> searchCompanies(Pageable pageable, CompanySearchCondition condition) {
		return companyRepository.search(condition, pageable)
			.map(this::toDto);
	}

	@Transactional
	public void deleteCompany(UUID companyId) {
		Company company = companyRepositoryHelper.findOrThrowNotFound(companyId);
		company.softDelete();
		companyRepository.save(company);
	}

	private CompanyDto.Result toDto(Company company) {
		return CompanyDto.Result.builder()
			.id(company.getId())
			.name(company.getName())
			.type(company.getType())
			.addressId(company.getAddressId())
			.build();
	}
}
