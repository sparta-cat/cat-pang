package com.catpang.hubproduct.domain.model;

import com.catpang.core.domain.model.auditing.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hub_products")
public class HubProduct extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_product_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private UUID hubId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private int amount;  // 상품 수량
}
