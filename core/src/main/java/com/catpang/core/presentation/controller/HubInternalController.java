package com.catpang.core.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;

import com.catpang.core.application.dto.HubDto;
import com.catpang.core.application.response.ApiResponse;

public interface HubInternalController {

	/**
	 * 허브 ID로 특정 회사를 조회하는 메서드
	 *
	 * @param id 조회할 허브의 ID
	 * @return 성공적인 응답과 조회된 허브 정보
	 */

	ApiResponse<HubDto.Result> getHub(@PathVariable UUID id);

}