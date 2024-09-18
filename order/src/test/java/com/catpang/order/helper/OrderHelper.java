package com.catpang.order.helper;

import static com.catpang.core.application.dto.OrderDto.*;
import static com.catpang.core.infrastructure.util.ArbitraryField.*;

import com.catpang.order.domain.model.Order;

public class OrderHelper {

	public static Order anOrder() {
		return Order.builder()
			.ownerId(OWNER_ID)
			.orderCompanyId(ORDER_COMPANY_ID)
			.build()
			.withId(ORDER_ID);
	}

	public static Create anOrderCreateDto() {
		return Create.builder()
			.build();
	}

	public static Put anOrderUpdateDto() {
		return Put.builder()
			.build();
	}

	public static Result.Single anOrderResultDto() {
		return Result.Single.builder()
			.id(ORDER_ID)
			.orderCompanyId(ORDER_COMPANY_ID)
			.ownerId(OWNER_ID)
			.totalQuantity(TOTAL_QUANTITY)
			.build();
	}

}
