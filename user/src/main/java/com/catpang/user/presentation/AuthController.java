package com.catpang.user.presentation;

import com.catpang.core.application.dto.UserDto;
import com.catpang.user.application.service.TokenService;
import com.catpang.user.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 및 토큰 관련 요청을 처리하는 컨트롤러 클래스입니다.
 */
@Slf4j(topic = "Auth Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth API", description = "인증, 인가 관련 API")
public class AuthController {

    private final UserService authService;
    private final TokenService tokenService;

    /**
     * 새로운 사용자를 회원가입합니다.
     *
     * @param create 새로운 사용자 생성에 필요한 정보를 담은 {@link UserDto.Create} 객체
     */
    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "새로운 사용자를 회원가입합니다.")
    public void join(@RequestBody UserDto.Create create) {
        authService.join(create);
    }

    /**
     * 토큰을 재발급합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     */
    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "만료된 액세스 토큰을 재발급합니다.")
    public void reIssue(HttpServletRequest request, HttpServletResponse response) {
        tokenService.reIssue(request, response);
    }

    /**
     * 토큰을 검증하고 사용자 정보를 반환합니다.
     *
     * @param headers 요청의 HTTP 헤더 정보
     * @return {@link UserDto.ForAddHeader} 인증된 사용자 정보
     */
    @PostMapping("/authorize")
    @Operation(summary = "토큰 검증 및 사용자 정보 반환", description = "JWT 토큰을 검증하고 인증된 사용자 정보를 반환합니다.")
    public UserDto.ForAddHeader authorize(@RequestHeader HttpHeaders headers) {
        return tokenService.authorize(headers);
    }
}
