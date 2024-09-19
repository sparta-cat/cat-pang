package com.catpang.core.domain.model.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.catpang.core.application.service.EntityMapper.getUserId;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped implements Serializable {

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@CreatedBy
	private Long createdBy;

	@LastModifiedDate
	@Column
	private LocalDateTime updatedAt;

	@LastModifiedBy
	private Long updatedBy;

	@Setter
	@Column
	private LocalDateTime deletedAt;

	@Setter
	private Long deletedBy;

	@Setter
	private Boolean isDeleted = false;

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
		this.isDeleted = true;
		this.deletedBy = getUserId(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	public void setCreatedBy(String creater) {
		this.createdBy = Long.valueOf(creater);
		this.updatedBy = Long.valueOf(creater);
	}
}