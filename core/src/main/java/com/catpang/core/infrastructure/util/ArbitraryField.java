package com.catpang.core.infrastructure.util;

import java.time.LocalTime;
import java.util.UUID;

public abstract class ArbitraryField {

	public static final UUID UUID_ID = UUID.fromString("efde71a2-398d-4543-b076-0e6d0328c3c9");
	public static final String NAME = "NAME";
	public static final String PRODUCT_NAME = "PRODUCT_NAME";
	public static final String MOBILE_NUMBER = "010-1234-5678";
	public static final String ADDRESS = "ADDRESS";
	public static final LocalTime BUSINESS_START_HOURS = LocalTime.of(8, 30);
	public static final LocalTime BUSINESS_END_HOURS = LocalTime.of(22, 0);
	public static final String IMAGE_URL = "IMAGE_URL";

	public static final Long USER_ID = 78L;
	public static final String PASSWORD = "!P@ssW0rd";
	public static final String EMAIL = "test@email.com";

	public static final Integer PRICE = 567000;
	public static final String DESCRIPTION = "Description";

	public static final String TITLE = "Title";
	public static final String CONTENT = "Contents";
}
