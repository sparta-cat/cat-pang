package com.catpang.core.domain;

public interface UserDto {
    record Create(
            String name,
            String nickname,
            String password,
            String email,
            String roleCode,
            String deliveryType
    ) {
    }
}
