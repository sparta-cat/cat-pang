package com.catpang.core.domain;

public record JoinCreate(
        String name,
        String nickname,
        String password,
        String email,
        String roleCode,
        Boolean isHubDeliveryManager,
        Boolean isCompanyDeliveryManager
) {
}
