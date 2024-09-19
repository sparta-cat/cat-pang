package com.catpang.hub.presentation.internal;

import com.catpang.core.application.response.ApiResponse;
import com.catpang.hub.application.dto.HubDto;
import com.catpang.hub.application.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/internal/hubs")
@RequiredArgsConstructor
public class HubInternalControllerImpl implements HubInternalController {

	private final HubService hubService;

	/**
	 * 허브 ID로 특정 허브를 조회하는 메서드
	 *
	 * @param hubId 조회할 허브의 ID
	 * @return 성공적인 응답과 조회된 허브 정보
	 */
	@GetMapping("/{hubId}")
	public ApiResponse<HubDto.Result> getHub(@PathVariable UUID hubId) {
		return ApiResponse.Success.<HubDto.Result>builder()
			.result(hubService.getHub(hubId))
			.build();
	}
}
