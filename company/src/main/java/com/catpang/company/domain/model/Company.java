package com.catpang.company.domain.model;

import com.catpang.core.domain.model.auditing.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "companies")
public class Company extends Timestamped {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "company_id", updatable = false, nullable = false, columnDefinition = "uuid")
	private UUID id;

	@Column(length = 100, nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(length = 50, nullable = false)
	private CompanyType type;

	@Column(nullable = false)
	private UUID addressId;

	@Column(nullable = false)
	private UUID ownerId;

	@Builder.Default
	private boolean isDeleted = false;

	public void softDelete() {
		this.isDeleted = true;
	}

	public void update(String name, CompanyType type, UUID addressId) {
		this.name = name;
		this.type = type;
		this.addressId = addressId;
	}
}
