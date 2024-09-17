package com.catpang.order;

import static com.catpang.core.infrastructure.util.ArbitraryField.*;
import static com.catpang.order.helper.OrderHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.AuditorAware;

import com.catpang.order.domain.model.Order;
import com.catpang.order.domain.repository.OrderRepository;

@SpringBootTest
class OrderAuditingTests {

	@Autowired
	private OrderRepository orderRepository;

	@MockBean
	private AuditorAware<Long> auditorAware;

	/**
	 * 테스트 전에 실행되며, 현재 인증된 사용자의 ID를 Mock으로 설정합니다.
	 */
	@BeforeEach
	void setUp() {
		// 현재 인증된 사용자 ID를 1L로 설정
		given(auditorAware.getCurrentAuditor()).willReturn(Optional.of(USER_ID));
	}

	/**
	 * 주문 생성 시, `CreatedBy` 및 `CreatedDate` 필드가 올바르게 설정되는지 테스트합니다.
	 *
	 * <p>
	 * <b>When</b>: 주문을 저장<br>
	 * <b>Then</b>: JPA Auditing 필드(`CreatedBy`, `CreatedAt`)가 올바르게 설정되는지 확인
	 * </p>
	 */
	@Test
	@DisplayName("주문 생성 시 CreatedBy, CreatedDate 필드가 올바르게 설정되는지 테스트")
	void 주문_생성시_CreatedBy_CreatedDate_설정_테스트() {
		// When: 주문을 저장
		Order savedOrder = orderRepository.save(anOrder());

		// Then: JPA Auditing 필드가 올바르게 설정되었는지 확인
		assertNotNull(savedOrder.getCreatedAt(), "CreatedAt 필드가 null이 아니어야 합니다.");
		assertEquals(USER_ID, savedOrder.getCreatedBy(), "CreatedBy 필드가 사용자 ID와 일치해야 합니다.");
	}

	/**
	 * 주문 수정 시, `LastModifiedBy` 및 `LastModifiedDate` 필드가 올바르게 설정되는지 테스트합니다.
	 *
	 * <p>
	 * <b>Given</b>: 주문을 먼저 저장한 후<br>
	 * <b>When</b>: 주문을 수정<br>
	 * <b>Then</b>: JPA Auditing 필드(`LastModifiedBy`, `LastModifiedAt`)가 올바르게 설정되는지 확인
	 * </p>
	 */
	@Test
	@DisplayName("주문 수정 시 LastModifiedBy, LastModifiedDate 필드가 올바르게 설정되는지 테스트")
	void 주문_수정시_LastModifiedBy_LastModifiedDate_설정_테스트() {
		// Given: 주문을 먼저 저장
		Order savedOrder = orderRepository.save(anOrder());

		// When: 주문을 수정 (예: totalQuantity 변경)
		savedOrder.setTotalQuantity(PRICE + 100);
		Order updatedOrder = orderRepository.save(savedOrder);

		// Then: LastModifiedBy와 LastModifiedDate 필드가 올바르게 설정되었는지 확인
		assertNotNull(updatedOrder.getUpdatedAt(), "UpdatedAt 필드가 null이 아니어야 합니다.");
		assertEquals(USER_ID, updatedOrder.getUpdatedBy(), "UpdatedBy 필드가 사용자 ID와 일치해야 합니다.");
	}
}