package com.catpang.order;

import static com.catpang.core.application.dto.OrderDto.*;
import static com.catpang.core.codes.SuccessCode.*;
import static com.catpang.core.infrastructure.util.ArbitraryField.*;
import static com.catpang.order.helper.OrderHelper.*;
import static com.catpang.order.helper.OrderProductHelper.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.data.domain.Sort.Direction.*;
import static org.springframework.data.domain.Sort.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.catpang.core.application.dto.CompanyDto;
import com.catpang.core.application.dto.OrderDto.Result.Single;
import com.catpang.core.application.dto.OrderProductDto;
import com.catpang.core.application.dto.ProductDto;
import com.catpang.core.application.response.ApiResponse;
import com.catpang.core.exception.CustomException;
import com.catpang.core.infrastructure.util.H2DbCleaner;
import com.catpang.core.presentation.controller.ProductInternalController;
import com.catpang.order.application.service.OrderService;
import com.catpang.order.domain.model.Order;
import com.catpang.order.domain.model.OrderProduct;
import com.catpang.order.domain.repository.OrderProductRepository;
import com.catpang.order.domain.repository.OrderRepository;
import com.catpang.order.domain.repository.OrderSearchCondition;
import com.catpang.order.infrastructure.feign.FeignCompanyInternalController;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest(classes = {OrderApplication.class}, properties = {"spring.cloud.config.enabled=false",
	// Config 서버 비활성화
	"eureka.client.enabled=false"           // Eureka 클라이언트 비활성화
})
@ComponentScan(basePackages = {"com.catpang.order", "com.catpang.core"})
class OrderServiceTests {

	@MockBean
	private FeignCompanyInternalController companyController;

	@MockBean
	private ProductInternalController productController;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderService orderService;

	@BeforeEach
	void setUp() throws SQLException {

		// SecurityContext와 Authentication을 Mocking
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);

		// 인증된 사용자 이름으로 "USER_ID"를 반환
		given(securityContext.getAuthentication()).willReturn(authentication);
		given(authentication.getName()).willReturn(USER_ID.toString());

		CompanyDto.Result companyResult = CompanyDto.Result.builder()
			.id(COMPANY_ID)
			.companyName(NAME)
			.companyAddress(ADDRESS)
			.companyPhone(MOBILE_NUMBER)
			.build();

		ApiResponse.Success<CompanyDto.Result> companyResponse = ApiResponse.Success.<CompanyDto.Result>builder()
			.result(companyResult)
			.successCode(SELECT_SUCCESS)
			.build();

		// Mocking FeignClient 호출
		given(companyController.getCompany(any(UUID.class))).willReturn(companyResponse);

		// Mock Product 호출
		ProductDto.Result productResult = ProductDto.Result.builder()
			.id(PRODUCT_ID)
			.name(PRODUCT_NAME)
			.price(PRICE)
			.companyId(COMPANY_ID)
			.build();

		// FeignClient 모킹 - Product 조회
		ApiResponse.Success<ProductDto.Result> productResponse = ApiResponse.Success.<ProductDto.Result>builder()
			.successCode(SELECT_SUCCESS)
			.result(productResult)
			.build();
		given(productController.getProduct(any(UUID.class))).willReturn(productResponse);

		H2DbCleaner.clean(dataSource);
	}

	// 기본 테스트 데이터를 저장하는 메서드 TODO: 반드시 helper 클래스로 리펙토링 할 것
	private void saveTestOrdersWithOwnerId(Long ownerId) {
		List<Order> orders = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			orders.add(anOrder().withOwnerId(ownerId).withTotalQuantity(PRICE + i));
		}
		orderRepository.saveAll(orders);
	}

	/**
	 * 주문 생성 관련 테스트 클래스
	 */
	@Nested
	class CreateOrderTests {

		@Test
		void 유효한_데이터로_주문_생성시_성공() {
			// Given
			Create createOrderDto = Create.builder()
				.orderCompanyId(ORDER_COMPANY_ID)
				.ownerId(USER_ID)
				.produceCompanyId(COMPANY_ID)
				.orderProductDtoes(
					List.of(anOrderProductCreateDto(), anOrderProductCreateDto(), anOrderProductCreateDto()))
				.build();

			// When
			Result.With<OrderProductDto.Result> result = orderService.createOrder(Pageable.unpaged(), createOrderDto);

			// Then
			assertNotNull(result);
			assertEquals(ORDER_COMPANY_ID, result.orderCompanyId());
			assertEquals(COMPANY_ID, result.produceCompanyId());
			assertEquals(USER_ID, result.ownerId());
		}

		@Test
		void 제품의_업체ID가_주문_업체ID와_일치하지_않아_ProductCompanyMismatchException_발생() {
			// Given
			Create createOrderDto = Create.builder()
				.orderCompanyId(ORDER_COMPANY_ID) // 주문의 업체 ID
				.ownerId(USER_ID)
				.produceCompanyId(COMPANY_ID) // 주문 생성 시 업체 ID
				.orderProductDtoes(
					List.of(anOrderProductCreateDto(), anOrderProductCreateDto(), anOrderProductCreateDto()))
				.build();

			// Mocking: 첫 번째 product는 주문의 업체 ID와 일치하고, 두 번째 product는 다른 업체 ID를 가지도록 설정
			ProductDto.Result validProductResult = ProductDto.Result.builder()
				.id(PRODUCT_ID)
				.name(PRODUCT_NAME)
				.price(PRICE)
				.companyId(COMPANY_ID) // 주문의 업체 ID와 일치
				.build();

			ProductDto.Result invalidProductResult = ProductDto.Result.builder()
				.id(PRODUCT_ID)
				.name(PRODUCT_NAME)
				.price(PRICE)
				.companyId(UUID.randomUUID()) // 다른 업체 ID
				.build();

			// Mock Product 호출
			given(productController.getProduct(any(UUID.class)))
				.willReturn(ApiResponse.Success.<ProductDto.Result>builder()
					.successCode(SELECT_SUCCESS)
					.result(validProductResult)
					.build())
				.willReturn(ApiResponse.Success.<ProductDto.Result>builder()
					.successCode(SELECT_SUCCESS)
					.result(invalidProductResult)
					.build());

			// When & Then
			CustomException.ProductCompanyMismatchException exception = assertThrows(
				CustomException.ProductCompanyMismatchException.class, () -> {
					orderService.createOrder(Pageable.unpaged(), createOrderDto);
				});

			assertEquals(String.format("Order company ID (%s) does not match Product company ID (%s)",
				COMPANY_ID, invalidProductResult.companyId()), exception.getMessage());
		}
	}

	/**
	 * 주문 조회 관련 테스트 클래스
	 */
	@Nested
	class ReadOrderTests {

		@Test
		void 유효한_주문ID로_조회시_성공() {
			// Given
			Order savedOrder = orderRepository.save(anOrder());

			// When
			Single result = orderService.readOrder(savedOrder.getId());

			// Then
			assertNotNull(result);
			assertEquals(savedOrder.getTotalQuantity(), result.totalQuantity());
			assertEquals(savedOrder.getOwnerId(), result.ownerId());
		}

		@Test
		void 존재하지_않는_주문ID로_조회시_실패() {
			// When & Then
			assertThrows(EntityNotFoundException.class, () -> {
				orderService.readOrder(ORDER_ID);
			});
		}
	}

	/**
	 * 주문 삭제 관련 테스트 클래스
	 */
	@Nested
	class DeleteOrderTests {

		@Test
		void 유효한_주문ID로_주문_삭제시_성공() {
			// Given
			Order order = anOrder();
			Order savedOrder = orderRepository.save(order);

			// When
			orderService.deleteOrder(savedOrder.getId());

			// Then
			assertThrows(EntityNotFoundException.class, () -> {
				orderService.readOrder(savedOrder.getId());
			});
		}

		@Test
		void 존재하지_않는_주문ID로_삭제시_실패() {
			// When & Then
			assertThrows(EntityNotFoundException.class, () -> {
				orderService.deleteOrder(ORDER_ID);
			});
		}

		@Test
		void 주문_삭제시_관련_주문상품들이_softDelete_전파되는지_검증(@Autowired OrderProductRepository orderProductRepository) {
			// Given
			Order order = orderRepository.save(anOrder());

			// OrderProduct 두 개를 해당 Order에 저장
			OrderProduct orderProduct1 = orderProductRepository.save(
				anOrderProduct().withOrder(order).withQuantity(10));
			OrderProduct orderProduct2 = orderProductRepository.save(
				anOrderProduct().withOrder(order).withQuantity(20));

			// When: Order를 softDelete
			orderService.softDeleteOrder(order.getId());

			// Then: 해당 Order에 속한 OrderProduct들이 softDelete 되었는지 검증
			OrderProduct foundOrderProduct1 = orderProductRepository.findById(orderProduct1.getId()).orElseThrow();
			OrderProduct foundOrderProduct2 = orderProductRepository.findById(orderProduct2.getId()).orElseThrow();

			assertThat(foundOrderProduct1.getIsDeleted(), is(true));
			assertThat(foundOrderProduct2.getIsDeleted(), is(true));
		}

	}

	@Nested
	class SearchOrderTests { //TODO: 개선필요

		@Test
		void 조건에_따라_주문_검색시_성공() {
			// Given
			saveTestOrdersWithOwnerId(USER_ID);
			Pageable pageable = PageRequest.of(0, 10);

			OrderSearchCondition condition = OrderSearchCondition.builder().ownerIds(List.of(USER_ID, 2L)).build();

			// When
			Page<Single> resultPage = orderService.searchOrder(pageable, condition);

			// Then
			assertNotNull(resultPage);
			assertEquals(5, resultPage.getTotalElements());
		}

		@Test
		void 정렬과_페이징으로_주문_검색_성공() {
			// Given
			saveTestOrdersWithOwnerId(USER_ID);
			Pageable pageable = PageRequest.of(0, 10, by(ASC, "totalQuantity"));

			OrderSearchCondition condition = OrderSearchCondition.builder().ownerIds(List.of(USER_ID, 2L)).build();

			// When
			Page<Single> resultPage = orderService.searchOrder(pageable, condition);

			// Then
			assertNotNull(resultPage);
			assertEquals(5, resultPage.getTotalElements());

			// assertTrue 대신 assertThat을 사용하여 가독성을 개선
			assertThat(resultPage.getContent().get(0).totalQuantity(),
				lessThanOrEqualTo(resultPage.getContent().get(1).totalQuantity()));
		}
	}

	/**
	 * 모든 주문 조회 테스트 클래스 (관리자 여부에 따른 필터링)
	 */
	@Nested
	class ReadAllOrdersTests { //TODO: 개선필요

		@Test
		void 관리자로_모든_주문_조회시_성공() {
			// Given
			saveTestOrdersWithOwnerId(USER_ID);
			Pageable pageable = PageRequest.of(0, 10);

			// When
			Page<Single> resultPage = orderService.readOrderAll(pageable, true, null);

			// Then
			assertNotNull(resultPage);
			assertEquals(5, resultPage.getTotalElements());
		}

		@Test
		void 소유자로_특정_주문_조회시_성공() {
			// Given
			saveTestOrdersWithOwnerId(USER_ID);
			Pageable pageable = PageRequest.of(0, 10);

			// When
			Page<Single> resultPage = orderService.readOrderAll(pageable, false, USER_ID);

			// Then
			assertNotNull(resultPage);
			assertEquals(5, resultPage.getTotalElements());
		}
	}
}