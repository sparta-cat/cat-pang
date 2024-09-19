package com.catpang.core.application.dto;

import lombok.Builder;
import lombok.With;

import java.util.UUID;

public interface HubDto {

    @With
    @Builder
    record Result(UUID id, String name, UUID addressId

    ) {

    }
}
