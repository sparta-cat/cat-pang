package com.sparta.gateway.config;


public interface UserDto {

    record ForAddHeader(
            String userId,
            String role
    ) {
    }
}
