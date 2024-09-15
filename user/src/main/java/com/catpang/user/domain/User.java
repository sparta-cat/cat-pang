package com.catpang.user.domain;

import com.catpang.core.domain.model.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn(name = "delivery_manager_type")
@Table(name = "p_users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String nickname;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String email;
	@Enumerated(EnumType.STRING)
	private RoleEnum role;
}
