package com.catpang.user.application.service;

import com.catpang.core.application.dto.UserDto;
import com.catpang.user.domain.model.User;
import com.catpang.user.domain.repository.UserRepository;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j(topic = "Slack Service")
@Service
@RequiredArgsConstructor
public class SlackService {

    private final UserRepository userRepository;
    private final UserMapperService userMapperService;
    @Value("${slack.bot.token}")
    private String slackBotToken; // 발급 받은 OAuth 토큰
    @Value("${slack.channel.id}")
    private String channelId; // 채널 id

    @Transactional
    public void sendMessage(UserDto.Slack slack) {

        User user = userRepository.findBySlackId(slack.receiverId()).orElseThrow(
                () -> new NoSuchElementException("해당 id를 가진 사용자가 없습니다.")
        );

        Slack slackApi = Slack.getInstance();
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(channelId)
                .text(slack.message())
                .build();

        try {
            ChatPostMessageResponse response = slackApi.methods(slackBotToken).chatPostMessage(request);
            if (response.isOk()) {
                log.info("메시지 전송 성공");
                user.addSlackMessage(userMapperService.fromSlackDto(slack));

            } else {
                log.error("메시지 전송 실패. Error: {}, Needed: {}, Provided: {}",
                        response.getError(), response.getNeeded(), response.getProvided());
            }
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
