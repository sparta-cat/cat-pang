package com.catpang.core.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.With;

import java.time.LocalDateTime;

public interface UserDto {

    interface Delete {
        @With
        @Builder
        @Schema(description = "사용자 삭제 결과 DTO")
        record Result(
                @Schema(description = "사용자 ID")
                Long id,
                @Schema(description = "삭제를 요청한 사용자 ID")
                Long deleterId,
                @Schema(description = "삭제된 시간")
                LocalDateTime deletedAt
        ) {
        }
    }

    @Schema(description = "사용자 생성 요청 DTO")
    record Create(
            @Schema(description = "사용자 이름")
            String name,
            @Schema(description = "닉네임")
            String nickname,
            @Schema(description = "비밀번호")
            String password,
            @Schema(description = "이메일")
            String email,
            @Schema(description = "권한 코드")
            String roleCode,
            @Schema(description = "배송 타입")
            String deliveryType,
            @Schema(description = "허브 ID")
            String hubId,
            @Schema(description = "슬랙 ID")
            String slackId
    ) {
    }

    @Schema(description = "로그인 요청 DTO")
    record Login(
            @Schema(description = "닉네임")
            String nickname,
            @Schema(description = "비밀번호")
            String password
    ) {
    }

    @Schema(description = "헤더에 추가할 사용자 정보 DTO")
    record ForAddHeader(
            @Schema(description = "사용자 ID")
            String userId,
            @Schema(description = "사용자 권한")
            String role
    ) {
    }

    @Schema(description = "사용자 정보 업데이트 요청 DTO")
    record Update(
            @Schema(description = "사용자 이름")
            String name,
            @Schema(description = "이메일")
            String email,
            @Schema(description = "사용자 권한")
            String role
    ) {
    }

    @Builder
    @Schema(description = "사용자 응답 DTO")
    record Result(
            @Schema(description = "사용자 ID")
            Long id,
            @Schema(description = "사용자 이름")
            String name,
            @Schema(description = "닉네임")
            String nickname,
            @Schema(description = "이메일")
            String email,
            @Schema(description = "사용자 권한")
            String role,
            @Schema(description = "슬랙 ID")
            String slackId
    ) {
    }

    @Schema(description = "슬랙 메시지 전송 요청 DTO")
    record Slack(
            @Schema(description = "수신자 ID")
            String receiverId,
            @Schema(description = "메시지 내용")
            String message
    ) {
    }

    @Builder
    @Schema(description = "캐싱용 사용자 정보 DTO")
    record CachingInfo(
            @Schema(description = "사용자 ID")
            String userId,
            @Schema(description = "사용자 권한")
            String role,
            @Schema(description = "닉네임")
            String nickname
    ) {
    }
}
