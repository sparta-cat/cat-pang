package com.catpang.core.infrastructure.util;

import java.time.LocalTime;
import java.util.UUID;

import com.catpang.core.domain.model.RoleEnum;

public abstract class ArbitraryField {

	/**
	 * Order 도메인
	 */

	/** Order 엔티티의 ID */
	public static final UUID ORDER_ID = UUID.fromString("d4e50d1e-2c84-41e9-8f5c-bb5b62368c91");

	/** Order의 총 수량 */
	public static final Integer TOTAL_QUANTITY = 87;

	/**  회사 ID */
	public static final UUID COMPANY_ID = UUID.fromString("8a7f60a7-5463-4674-837e-70bf223a9294");

	/** 주문한 회사 ID */
	public static final UUID ORDER_COMPANY_ID = UUID.fromString("f71f0b2e-c9ac-4771-b6cd-d189e3dd2712");

	/** 주문 소유자 ID */
	public static final Long OWNER_ID = 99L;

	/**
	 * OrderProduct 도메인
	 */

	/** OrderProduct 엔티티의 ID */
	public static final UUID ORDER_PRODUCT_ID = UUID.fromString("a7f7607a-89b9-4c44-910e-f85f64c14b4d");

	/** 제품의 수량 */
	public static final Integer PRODUCT_QUANTITY = 5;

	/** 제품 ID */
	public static final UUID PRODUCT_ID = UUID.fromString("12ef761a-89f9-4c64-b18f-56d07a6b4e3b");

	/**
	 * User 도메인
	 */

	/** User 엔티티의 ID */
	public static final Long USER_ID = 78L;

	/** 사용자 이름 */
	public static final String USER_NAME = "John Doe";

	/** 사용자 닉네임 */
	public static final String USER_NICKNAME = "Johnny";

	/** 사용자 비밀번호 */
	public static final String USER_PASSWORD = "!P@ssW0rd";

	/** 사용자 이메일 */
	public static final String USER_EMAIL = "john.doe@email.com";

	/** 사용자 권한 */
	public static final RoleEnum USER_ROLE = RoleEnum.HUB_CUSTOMER;

	/**
	 * 공통 필드
	 */

	/** 이름 */
	public static final String NAME = "NAME";

	/** 제품 이름 */
	public static final String PRODUCT_NAME = "PRODUCT_NAME";

	/** 휴대폰 번호 */
	public static final String MOBILE_NUMBER = "010-1234-5678";

	/** 주소 */
	public static final String ADDRESS = "ADDRESS";

	/** 사업 시작 시간 */
	public static final LocalTime BUSINESS_START_HOURS = LocalTime.of(8, 30);

	/** 사업 종료 시간 */
	public static final LocalTime BUSINESS_END_HOURS = LocalTime.of(22, 0);

	/** 이미지 URL */
	public static final String IMAGE_URL = "IMAGE_URL";

	/** 가격 */
	public static final Integer PRICE = 567000;

	/** 설명 */
	public static final String DESCRIPTION = "Description";

	/** 제목 */
	public static final String TITLE = "Title";

	/** 내용 */
	public static final String CONTENT = "Contents";
}