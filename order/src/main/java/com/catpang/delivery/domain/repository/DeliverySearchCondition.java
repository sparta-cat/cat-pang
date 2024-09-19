package com.catpang.delivery.domain.repository;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record DeliverySearchCondition(List<UUID> ids, List<Long> ownerIds, UUID orderId) {

    @Builder
    public DeliverySearchCondition(List<UUID> ids, List<Long> ownerIds, UUID orderId) {
        this.ids = ids == null ? new ArrayList<>() : ids;
        this.ownerIds = ownerIds == null ? new ArrayList<>() : ownerIds;
        this.orderId = orderId;
    }
}
