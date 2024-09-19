package com.catpang.order;

import com.catpang.core.application.dto.ProductDto;
import com.catpang.core.application.response.ApiResponse;
import com.catpang.core.infrastructure.util.H2DbCleaner;
import com.catpang.order.application.service.OrderProductService;
import com.catpang.order.domain.model.Order;
import com.catpang.order.domain.model.OrderProduct;
import com.catpang.order.domain.repository.OrderProductRepository;
import com.catpang.order.domain.repository.OrderProductSearchCondition;
import com.catpang.order.domain.repository.OrderRepository;
import com.catpang.order.infrastructure.feign.FeignProductInternalController;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.catpang.OrderApplication;
import com.catpang.core.application.dto.ProductDto;
import com.catpang.core.application.response.ApiResponse;
import com.catpang.core.infrastructure.util.H2DbCleaner;
import com.catpang.order.application.service.OrderProductService;
import com.catpang.order.domain.model.Order;
import com.catpang.order.domain.model.OrderProduct;
import com.catpang.order.domain.repository.OrderProductRepository;
import com.catpang.order.domain.repository.OrderProductSearchCondition;
import com.catpang.order.domain.repository.OrderRepository;
import com.catpang.order.infrastructure.feign.FeignProductInternalController;

import static com.catpang.core.application.dto.OrderProductDto.Create;
import static com.catpang.core.application.dto.OrderProductDto.Result;
import static com.catpang.core.codes.SuccessCode.SELECT_SUCCESS;
import static com.catpang.core.infrastructure.util.ArbitraryField.*;
import static com.catpang.order.helper.OrderHelper.anOrder;
import static com.catpang.order.helper.OrderProductHelper.anOrderProduct;
import static com.catpang.order.helper.OrderProductHelper.anOrderProductCreateDto;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {OrderApplication.class}, properties = {
	"spring.cloud.config.enabled=false",
	"eureka.client.enabled=false"
})
@ComponentScan(basePackages = {"com.catpang.order", "com.catpang.core"})
class OrderProductServiceTests {

	@MockBean
	private FeignProductInternalController productController;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private OrderProductService orderProductService;

	@Autowired
	private OrderRepository orderRepository;

	@BeforeEach
	void setUp() throws Exception {
		// Mock Product 호출
		ProductDto.Result productResult = ProductDto.Result.builder()
			.id(PRODUCT_ID)
			.name(PRODUCT_NAME)
			.price(PRICE)
			.companyId(COMPANY_ID)
			.build();

		// FeignClient 모킹 - Product 조회
		given(productController.getProduct(any(UUID.class)))
			.willReturn(ApiResponse.Success.<ProductDto.Result>builder()
				.successCode(SELECT_SUCCESS)
				.result(productResult)
				.build());

		H2DbCleaner.clean(dataSource);
	}

	/**
	 * 주문상품 생성 및 Order와의 1:N 관계를 검증하는 테스트
	 */
	@Nested
	class CreateOrderProductTests {

		@Test
		void 유효한_데이터로_주문상품_생성시_성공_및_Order와_1_N_관계_검증() {
			// Given
			Order order = orderRepository.save(anOrder());
			Create createDto = anOrderProductCreateDto();

			// When
			Page<Result> results = orderProductService.createOrderProducts(Pageable.unpaged(), List.of(createDto),
				order.getId(), COMPANY_ID);

			// Then
			OrderProduct found = orderProductRepository.findById(results.getContent().get(0).id())
				.orElseThrow(EntityNotFoundException::new);

			// 검증: ID가 존재하는지 확인
			assertThat(results, is(notNullValue()));
			assertThat(found.getId(), is(equalTo(results.getContent().get(0).id())));

			// OrderProduct가 Order를 참조하는지 확인
			assertThat(found.getOrder(), is(notNullValue()));
			assertThat(found.getOrder().getId(), is(equalTo(order.getId())));
		}
	}

	/**
	 * 주문상품 조회 시 Order와의 관계를 검증하는 테스트
	 */
	@Nested
	class ReadOrderProductTests {

		@Test
		void 유효한_ID로_주문상품_조회시_성공_및_Order와_1_N_관계_검증() {
			// Given
			Order order = orderRepository.save(anOrder());
			OrderProduct savedOrderProduct = orderProductRepository.save(anOrderProduct().withOrder(order));

			// When
			Result result = orderProductService.readOrderProduct(savedOrderProduct.getId());

			// Then
			OrderProduct found = orderProductRepository.findById(result.id())
				.orElseThrow(EntityNotFoundException::new);

			// 검증: ID 및 Order와의 관계 확인
			assertThat(result.id(), is(notNullValue()));
			assertThat(found.getOrder(), is(notNullValue()));
			assertThat(found.getOrder().getId(), is(equalTo(order.getId())));
		}

		@Test
		void 존재하지_않는_ID로_주문상품_조회시_실패() {
			// When & Then
			assertThrows(EntityNotFoundException.class, () -> {
				orderProductService.readOrderProduct(ORDER_PRODUCT_ID);
			});
		}
	}

	/**
	 * 주문상품 삭제 시 제대로 삭제되는지 검증하는 테스트
	 */
	@Nested
	class DeleteOrderProductTests {

		@Test
		void 유효한_ID로_주문상품_삭제시_성공() {
			// Given
			Order order = orderRepository.save(anOrder());
			OrderProduct savedOrderProduct = orderProductRepository.save(anOrderProduct().withOrder(order));

			// When
			orderProductService.deleteOrderProduct(savedOrderProduct.getId());

			// Then
			assertThrows(EntityNotFoundException.class, () -> {
				orderProductService.readOrderProduct(savedOrderProduct.getId());
			});
		}

		@Test
		void 존재하지_않는_ID로_삭제시_실패() {
			// When & Then
			assertThrows(EntityNotFoundException.class, () -> {
				orderProductService.deleteOrderProduct(ORDER_PRODUCT_ID);
			});
		}
	}

	/**
	 * 주문상품을 조건에 따라 검색할 수 있는지 검증하는 테스트
	 */
	@Nested
	class SearchOrderProductTests {

		@Test
		void 조건에_따라_주문상품_검색시_성공_및_페이징_검증() {
			// Given
			Pageable pageable = PageRequest.of(0, 10);

			// 실제 Order와 OrderProduct 더미 데이터를 간결하게 생성하여 저장
			Order order1 = orderRepository.save(anOrder().withOwnerId(1L));
			Order order2 = orderRepository.save(anOrder().withOwnerId(2L));

			OrderProduct orderProduct1 = orderProductRepository.save(
				anOrderProduct().withOrder(order1).withQuantity(10)
			);
			OrderProduct orderProduct2 = orderProductRepository.save(
				anOrderProduct().withOrder(order2).withQuantity(20)
			);

			// 검색 조건: 특정 ID와 ownerId 리스트를 가진 조건 생성
			List<UUID> ids = List.of(orderProduct1.getId(), orderProduct2.getId());
			List<Long> ownerIds = List.of(1L, 2L);

			OrderProductSearchCondition condition = OrderProductSearchCondition.builder()
				.ids(ids)
				.ownerIds(ownerIds)
				.build();

			// When
			Page<Result> resultPage = orderProductService.searchOrderProduct(pageable, condition);

			// Then
			assertThat(resultPage, is(notNullValue()));
			assertThat(resultPage.getTotalElements(), is(equalTo(2L)));  // 검색된 요소는 2개여야 함
			assertThat(resultPage.getContent().size(), is(lessThanOrEqualTo(10)));  // 한 페이지에 최대 10개

			// 순서에 상관없이 검색된 OrderProduct들이 있는지 확인
			List<UUID> resultOrderIds = resultPage.map(Result::orderId).getContent();
			assertThat(resultOrderIds, containsInAnyOrder(order1.getId(), order2.getId()));
		}

		@Test
		void 조건에_맞는_주문상품이_존재하지_않을_경우_빈_결과_반환_및_페이징_검증() {
			// Given
			Pageable pageable = PageRequest.of(0, 10);

			// 검색 조건: 존재하지 않는 ID 및 ownerId 리스트를 가진 조건
			List<UUID> ids = List.of(UUID.randomUUID());
			List<Long> ownerIds = List.of(999L);

			OrderProductSearchCondition condition = OrderProductSearchCondition.builder()
				.ids(ids)
				.ownerIds(ownerIds)
				.build();

			// When
			Page<Result> resultPage = orderProductService.searchOrderProduct(pageable, condition);

			// Then
			assertThat(resultPage, is(notNullValue()));
			assertThat(resultPage.getTotalElements(), is(equalTo(0L)));  // 검색된 요소는 0이어야 함
			assertThat(resultPage.getContent().size(), is(equalTo(0)));  // 검색된 내용도 0이어야 함
		}
	}
}