package com.catpang.order.domain.model;

import static jakarta.persistence.FetchType.*;

import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import com.catpang.core.domain.model.auditing.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

@With
@Getter
@Entity
@Table(name = "p_order_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends Timestamped {

	@Id
	@UuidGenerator
	@Column(name = "order_product_id")
	private UUID id;

	@Setter
	@NotNull
	@Min(1)
	@Column(name = "quantity")
	private Integer quantity;

	@Setter
	@ToString.Exclude
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "order_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Order order;

	@Setter
	private UUID productId;

	@Builder
	public OrderProduct(Integer quantity, Order order, UUID productId) {
		this.quantity = quantity;
		this.order = order;
		this.productId = productId;
	}
}


