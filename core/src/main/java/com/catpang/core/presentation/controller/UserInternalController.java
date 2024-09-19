package com.catpang.core.presentation.controller;

import com.catpang.core.application.dto.UserDto;
import com.catpang.core.application.response.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserInternalController {

    /**
     * 유저 ID로 특정 회사를 조회하는 메서드
     *
     * @param id 조회할 유저의 ID
     * @return 성공적인 응답과 조회된 유저 정보
     */

    ApiResponse<UserDto.Result> getUser(@PathVariable Long id);

}