package com.catpang.core.application.dto;

import lombok.Builder;
import lombok.With;

import java.util.UUID;

public interface AddressDto {

    @With
    @Builder
    record Result(UUID id, String state, String detail, Double latitude, Double longitude

    ) {

    }
}
