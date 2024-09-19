package com.catpang.core.domain.model;

/**
 * 배송 상태를 나타내는 enum 클래스
 */
public enum DeliveryStatus {

    /**
     * 배송 대기 중
     */
    WAITING_FOR_TRANSPORT,

    /**
     * 배송 중
     */
    IN_TRANSIT,

    /**
     * 도착 완료
     */
    ARRIVED,

    /**
     * 배송 준비 중
     */
    OUT_FOR_DELIVERY
}