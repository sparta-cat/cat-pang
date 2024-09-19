package com.catpang.delivery.domain.model;

import com.catpang.core.domain.model.DeliveryStatus;
import com.catpang.core.domain.model.auditing.Timestamped;
import com.catpang.order.domain.model.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static com.catpang.core.domain.model.DeliveryStatus.WAITING_FOR_TRANSPORT;
import static jakarta.persistence.FetchType.LAZY;

@With
@Setter
@Getter
@Entity
@Table(name = "p_deliveries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends Timestamped {

    @Id
    @UuidGenerator
    @Column(name = "delivery_id")
    private UUID id;

    @NotNull
    @ToString.Exclude
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = WAITING_FOR_TRANSPORT;

    @NotNull
    private UUID departureHubId;

    @NotNull
    private UUID destinationHubId;

    @NotNull
    private UUID receiveCompanyId;

    @NotNull
    private Long receiverId;

    @NotNull
    private UUID receiverSlackId;

    @NotNull
    private UUID presentAddressId;

    @Builder
    public Delivery(Order order, UUID departureHubId, UUID destinationHubId,
                    UUID receiveCompanyId,
                    Long receiverId, UUID receiverSlackId, UUID presentAddressId) {
        this.order = order;
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.receiveCompanyId = receiveCompanyId;
        this.receiverId = receiverId;
        this.receiverSlackId = receiverSlackId;
        this.presentAddressId = presentAddressId;
    }
}


