package com.catpang.hub.domain.model;

import com.catpang.address.domain.model.Address;
import com.catpang.core.domain.model.auditing.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Hub 엔티티는 허브의 정보를 저장하는 클래스입니다.
 * PostgreSQL의 UUID 타입과 매핑됩니다.
 */
@Getter
@Setter
@Builder
@Entity
@Table(name = "hubs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Hub extends Timestamped {

	/**
	 * 허브의 고유 식별자입니다.
	 * PostgreSQL의 uuid 타입과 매핑됩니다.
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "hub_id", updatable = false, nullable = false, columnDefinition = "uuid")
	private UUID id;

	/**
	 * 허브의 이름입니다.
	 */
	@Column(length = 100, nullable = false)
	private String name;

	/**
	 * 허브의 주소입니다.
	 * Address 모듈의 Address 엔티티를 참조합니다.
	 */
	@ManyToOne
	@JoinColumn(name = "address_id", nullable = false)
	private Address address;

	/**
	 * 허브를 생성한 사용자의 ID입니다.
	 */
	@Column(name = "owner_id", nullable = false, columnDefinition = "uuid")
	private Long ownerId;
}
