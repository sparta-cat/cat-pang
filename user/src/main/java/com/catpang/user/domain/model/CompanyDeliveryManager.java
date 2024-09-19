package com.catpang.user.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_company_delivery_managers")
public class CompanyDeliveryManager {

    @Id
    private Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    // FeignClient를 통해 검증한 hubId 값
    @Column(nullable = false)
    private String hubId;
}
