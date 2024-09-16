package com.catpang.core.presentation.controller;

import static com.catpang.core.application.dto.CompanyDto.*;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;

import com.catpang.core.application.response.ApiResponse;

public interface CompanyInternalController {
	/**
	 * 회사 ID로 특정 회사를 조회하는 메서드
	 *
	 * @param id 조회할 회사의 ID
	 * @return 성공적인 응답과 조회된 회사 정보
	 */

	ApiResponse<Result> getCompany(@PathVariable UUID id);
}