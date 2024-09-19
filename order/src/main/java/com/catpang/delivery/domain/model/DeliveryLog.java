package com.catpang.delivery.domain.model;

import static jakarta.persistence.FetchType.*;

import java.time.Duration;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import com.catpang.core.domain.model.DeliveryStatus;
import com.catpang.core.domain.model.auditing.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Setter
@Getter
@Entity
@Table(name = "p_delivery_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryLog extends Timestamped {

	@Id
	@UuidGenerator
	@Column(name = "delivery_log_id")
	private UUID id;

	@ToString.Exclude
	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "delivery_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Delivery delivery;

	@NotNull
	private Integer sequence;

	@NotNull
	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	@NotNull
	private UUID departureHubId;

	@NotNull
	private UUID destinationHubId;

	private Double estimatedDistance;

	private Duration estimatedTime;

	private Double actualDistance;

	private Duration actualTime;

	@Builder
	public DeliveryLog(Delivery delivery, Integer sequence, DeliveryStatus status, UUID departureHubId,
		UUID destinationHubId, Double estimatedDistance, Duration estimatedTime, Double actualDistance,
		Duration actualTime) {
		this.delivery = delivery;
		this.sequence = sequence;
		this.status = status;
		this.departureHubId = departureHubId;
		this.destinationHubId = destinationHubId;
		this.estimatedDistance = estimatedDistance;
		this.estimatedTime = estimatedTime;
		this.actualDistance = actualDistance;
		this.actualTime = actualTime;
	}
}
