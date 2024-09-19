package com.catpang.user.application.service;

import com.catpang.core.application.dto.UserDto;
import com.catpang.core.domain.model.RoleEnum;
import com.catpang.user.domain.model.CompanyDeliveryManager;
import com.catpang.user.domain.model.HubDeliveryManager;
import com.catpang.user.domain.model.SlackMessage;
import com.catpang.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserMapperService {

    private final PasswordEncoder passwordEncoder;

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User toUserEntity(UserDto.Create userCreate, RoleEnum role) {
        return User.builder()
                .nickname(userCreate.nickname())
                .password(encodePassword(userCreate.password()))
                .name(userCreate.name())
                .email(userCreate.email())
                .role(role)
                .slackId(userCreate.slackId())
                .build();
    }

    /**
     * User를 먼저 만들어서 저장하고,
     * deliveryManager.setUser() 등으로 user설정
     * <p>
     * TODO: HUB ID를 입력받고, FeignClient로 존재하는 허브이면 값 설정.
     */
    public CompanyDeliveryManager toCompanyDeliveryManager(User user, String hubId) {
        return CompanyDeliveryManager.builder()
                .user(user)
                .hubId(hubId)
                .build();
    }

    public HubDeliveryManager toHubDeliveryManager(User user) {
        return HubDeliveryManager.builder()
                .user(user)
                .build();
    }

    public UserDto.Result fromUser(User user) {
        return UserDto.Result.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .build();
    }

    public UserDto.Delete.Result fromDeleter(User user, String deleterId) {
        return UserDto.Delete.Result.builder()
                .id(user.getId())
                .deleterId(Long.valueOf(deleterId))
                .deletedAt(user.getDeletedAt())
                .build();
    }

    public SlackMessage fromSlackDto(UserDto.Slack slack) {
        return SlackMessage.builder()
                .receiver(slack.receiverId())
                .message(slack.message())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
