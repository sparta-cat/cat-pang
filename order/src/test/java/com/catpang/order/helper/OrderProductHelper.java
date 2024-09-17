package com.catpang.order.helper;

import static com.catpang.core.application.dto.OrderProductDto.*;
import static com.catpang.core.infrastructure.util.ArbitraryField.*;
import static com.catpang.order.helper.OrderHelper.*;

import com.catpang.order.domain.model.OrderProduct;

public class OrderProductHelper {

	public static OrderProduct anOrderProduct() {
		return OrderProduct.builder()
			.quantity(PRODUCT_QUANTITY)
			.productId(PRODUCT_ID)
			.order(anOrder())
			.build()
			.withId(ORDER_PRODUCT_ID);
	}

	public static Create anOrderProductCreateDto() {
		return Create.builder()
			.quantity(PRODUCT_QUANTITY)
			.productId(PRODUCT_ID)
			.build();
	}

	public static Put anOrderProductUpdateDto() {
		return Put.builder()
			.build();
	}

	public static Result anOrderProductResultDto() {
		return Result.builder()
			.build();
	}

}
