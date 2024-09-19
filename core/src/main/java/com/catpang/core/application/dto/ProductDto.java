package com.catpang.core.application.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * ProductDto - 상품 관련 데이터 전송 객체
 */
public class ProductDto {

	@Getter
	@Setter
	@Builder
	public static class Create {
		private String productName;
		private UUID companyId;
		private UUID hubId;
	}

	@Getter
	@Setter
	@Builder
	public static class Put {
		private String productName;
	}

	@Getter
	@Setter
	@Builder
	public static class Result {
		private UUID id;
		private String productName;
		private UUID companyId;
		private UUID hubId;
	}
}
