package com.catpang.user.application.service;

import com.catpang.core.application.dto.UserDto;
import com.catpang.user.domain.model.RefreshToken;
import com.catpang.user.domain.repository.RefreshTokenRepository;
import com.catpang.user.infrastructure.jwt.JwtUtil;
import com.catpang.user.security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Access 토큰 추출 후 검증하는 메서드
     *
     * @param headers
     */
    public UserDto.ForAddHeader authorize(HttpHeaders headers) {
        String accessToken = jwtUtil.getAccessTokenFromHeader(headers);
        if (StringUtils.hasText(accessToken)) {
            if (!jwtUtil.isNotExpiredAccessToken(accessToken) || !jwtUtil.validateToken(accessToken)) {
                throw new RuntimeException("유효하지 않은 토큰");
            }
            // 토큰에서 userId 받아온 후
            String userIdFromAccessToken = jwtUtil.getUserIdFromAccessToken(accessToken);
            String userRoleFromAccessToken = jwtUtil.getUserRoleFromAccessToken(accessToken);
            // 캐싱처리. 재발급 시 db까지는 접근 X
            userDetailsService.loadUserById(userIdFromAccessToken);
            return new UserDto.ForAddHeader(userIdFromAccessToken, userRoleFromAccessToken);
        }
        return null;
    }

    @Transactional
    public void reIssue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.getRefreshTokenFromCookie(request);
        if (refreshToken == null || !refreshTokenRepository.existsByRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        if (jwtUtil.isNotExpiredRefreshToken(refreshToken)) {
            String userId = jwtUtil.getUserIdFromRefreshToken(refreshToken);
            // 재발급 시 캐싱처리
            userDetailsService.loadUserById(userId);

            String userRole = jwtUtil.getUserRoleFromRefreshToken(refreshToken);
            String reIssuedAccessToken = jwtUtil.createAccessToken(userId, userRole);
            String reIssuedRefreshToken = jwtUtil.createRefreshToken(userId, userRole);
            saveRefreshToken(userId, reIssuedRefreshToken);

            response.addHeader(jwtUtil.AUTHORIZATION_HEADER, reIssuedAccessToken);
            response.addCookie(jwtUtil.addRefreshTokenToCookie(reIssuedRefreshToken));
        }
    }

    @Transactional
    @CachePut(cacheNames = "refreshToken", key = "args[0]")
    public String saveRefreshToken(String userId, String refresh) {
        Date date = new Date(System.currentTimeMillis() + jwtUtil.REFRESH_TOKEN_TIME);
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .refreshToken(refresh)
                .expiration(date.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getRefreshToken();
    }
}
