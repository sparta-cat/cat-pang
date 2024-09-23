package com.catpang.product.domain.model;

import com.catpang.core.domain.model.auditing.Timestamped;
import com.catpang.product.application.dto.ProductDto.Update;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private UUID hubId;

    @Column(nullable = false)
    private int price;

    @Builder.Default
    private boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }

    // Update 메소드 추가
    public void update(Update updateDto) {
        this.name = updateDto.name();
        this.companyId = updateDto.companyId();
        this.hubId = updateDto.hubId();
        this.price = updateDto.price();
    }
}
