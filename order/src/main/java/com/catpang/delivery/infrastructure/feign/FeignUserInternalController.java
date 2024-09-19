package com.catpang.delivery.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.catpang.core.application.dto.UserDto;
import com.catpang.core.application.response.ApiResponse;
import com.catpang.core.presentation.controller.UserInternalController;

@FeignClient(name = "user-service")
public interface FeignUserInternalController extends UserInternalController {

	@GetMapping("/api/v1/internal/users/{id}")
	ApiResponse<UserDto.Result> getUser(@PathVariable Long id);
}

