package com.catpang.delivery.infrastructure.feign;

import com.catpang.core.application.dto.HubDto;
import com.catpang.core.application.response.ApiResponse;
import com.catpang.core.presentation.controller.HubInternalController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface FeignHubInternalController extends HubInternalController {

    @GetMapping("/api/v1/internal/hubs/{id}")
    ApiResponse<HubDto.Result> getHub(@PathVariable UUID id);

}

