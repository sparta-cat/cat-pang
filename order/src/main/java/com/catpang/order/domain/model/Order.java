package com.catpang.order.domain.model;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.catpang.core.domain.model.auditing.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@With
@Getter
@Entity
@Table(name = "p_orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Timestamped {

	@Id
	@UuidGenerator
	@Column(name = "order_id")
	private UUID id;

	@Setter
	@NotNull
	@Column(name = "total_quantity")
	private Integer totalQuantity = 0;

	@Setter
	@NotNull
	private UUID orderCompanyId;

	@NotNull
	private Long ownerId;

	@NotNull
	private UUID produceCompanyId;

	@Builder
	public Order(UUID orderCompanyId, Long ownerId, UUID produceCompanyId) {
		this.orderCompanyId = orderCompanyId;
		this.ownerId = ownerId;
		this.produceCompanyId = produceCompanyId;
	}
}


