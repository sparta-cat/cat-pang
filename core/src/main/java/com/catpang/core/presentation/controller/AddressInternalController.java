package com.catpang.core.presentation.controller;

import com.catpang.core.application.dto.AddressDto;
import com.catpang.core.application.response.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface AddressInternalController {

    /**
     * 주소 ID로 특정 회사를 조회하는 메서드
     *
     * @param id 조회할 주소의 ID
     * @return 성공적인 응답과 조회된 주소 정보
     */

    ApiResponse<AddressDto.Result> getAddress(@PathVariable UUID id);

}