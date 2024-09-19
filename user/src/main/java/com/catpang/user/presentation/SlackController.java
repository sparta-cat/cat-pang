package com.catpang.user.presentation;

import com.catpang.core.application.dto.UserDto;
import com.catpang.user.application.service.SlackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Slack 메시지 전송을 처리하는 컨트롤러 클래스입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/slack")
@Tag(name = "Slack API", description = "Slack 메시지 관련 API")
public class SlackController {

    private final SlackService slackService;

    /**
     * Slack에 메시지를 전송합니다.
     *
     * @param slack 전송할 메시지 정보를 담은 {@link UserDto.Slack} 객체
     */
    @PostMapping
    @Operation(summary = "Slack 메시지 전송", description = "Slack에 메시지를 전송합니다.")
    public void sendMessage(@RequestBody UserDto.Slack slack) {
        slackService.sendMessage(slack);
    }
}
