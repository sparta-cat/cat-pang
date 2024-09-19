package com.catpang.core.domain.model;

public enum DeliveryType {
    HUB_DELIVERY_MANAGER,
    COMPANY_DELIVERY_MANAGER;

    /**
     * deliveryType에 따른 enum값 반환
     *
     * @param deliveryType 전달받은 배송 담당 type
     * @return
     */
    public static DeliveryType fromType(String deliveryType) {
        for (DeliveryType type : DeliveryType.values()) {
            if (type.name().equalsIgnoreCase(deliveryType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid role code: " + deliveryType);
    }
}
