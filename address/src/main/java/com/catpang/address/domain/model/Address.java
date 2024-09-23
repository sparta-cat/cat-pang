package com.catpang.address.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

	@Column(nullable = false)
	private String zipCode;

    @Column(nullable = true)
    private String latitude;

    @Column(nullable = true)
    private String longitude;
}
