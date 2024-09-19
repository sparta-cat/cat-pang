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
@Table(name = "p_hub_delivery_managers")
public class HubDeliveryManager {
    @Id // pk로 지정
    private Long id;

    @MapsId // User엔티티의 pk를 재사용할 수 있도록
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;


}
