package com.catpang.hub.presentation.internal;

import com.catpang.hub.application.dto.HubDto;
import com.catpang.core.application.response.ApiResponse;

import java.util.UUID;

/**
 * HubInternalController는 내부 API에서 허브 정보를 처리하기 위한 인터페이스입니다.
 */
public interface HubInternalController {

	/**
	 * 허브 ID로 특정 허브를 조회하는 메서드
	 *
	 * @param hubId 조회할 허브의 ID
	 * @return 성공적인 응답과 조회된 허브 정보
	 */
	ApiResponse<HubDto.Result> getHub(UUID hubId);
}
