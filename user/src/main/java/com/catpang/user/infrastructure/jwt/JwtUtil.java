package com.catpang.user.infrastructure.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/*
 * JWT 발급과 검증을 담당하는 클래스
 * 가장 최신 방식인 0.12.3방식 사용.
 * 주의 11. 버전과 많은 차이가 있음!
 *
 */
@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // Refresh Token 만료기간
    public static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일
    // Access Token 만료기간
    private static final long ACCESS_TOKEN_TIME = 10 * 60 * 1000L * 144; // 10분
    private SecretKey secretKey;
    private SecretKey refreshSecretKey;

    /*
     * .env파일에 있는 키를 암호화해서 SecretKey객체 타입으로 저장
     * 양방향 암호화의 대칭키 방식 사용: 동일한 방식으로 암호화, 복호화 진행. cf. 비대칭키 방식으로도 가능
     *
     * */
    public JwtUtil(@Value("${jwt.secret.key}") String secret, @Value("${jwt.refresh.secret.key}") String refresh) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshSecretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // Access Token 생성
    public String createAccessToken(String userId, String role) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .claim("userId", userId) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발급일
                        .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME)) // 토큰 만료일
                        .signWith(secretKey) // 토큰 암호화
                        .compact();
    }

    // Refresh Token 생성
    // 엑세스 토큰의 갱신, 장기 세션 유지 등의 역할을 함
    public String createRefreshToken(String userId, String role) {
        return Jwts.builder()
                .claim("userId", userId) // 사용자 식별자값(ID)
                .claim(AUTHORIZATION_KEY, role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME)) // 토큰 만료일
                .signWith(refreshSecretKey) // 토큰 암호화
                .compact();
    }

    // Refresh Token 생성 후 Cookie에 담기
    public Cookie addRefreshTokenToCookie(String refreshToken) {
        // Cookie Value에는 공백이 불가능하므로 encoding 진행
        try {
            refreshToken = URLEncoder.encode(refreshToken, "utf-8").replaceAll("\\+", "%20");
            Cookie refreshTokenCookie = new Cookie("RefreshTokenCookie", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge((int) (REFRESH_TOKEN_TIME / 1000)); // 밀리 초가 아닌 초 단위기 때문에 /1000
            return refreshTokenCookie;
        } catch (UnsupportedEncodingException e) {
            log.error("Refresh Token encoding 오류: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Header에서 Access Token 가져오기
    public String getAccessTokenFromHeader(HttpHeaders headers) {
        String bearerToken = headers.getFirst(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7).trim(); // 순수한 토큰 가져오기 위해 substring(BEARER 자름)
        }
        return null;
    }

    // Cookie에서 Refresh Token 가져오기
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("RefreshTokenCookie")) {
                refresh = cookie.getValue();
            }
        }
        return refresh;
    }


    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다. 사용자 ID: {}", getUserIdFromAccessToken(token));
        } catch (MalformedJwtException | SecurityException e) {
            log.error("Invalid JWT token, 유효하지 않은 JWT token 입니다. 사용자 ID: {}", getUserIdFromAccessToken(token));
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다. 사용자 ID: {}", getUserIdFromAccessToken(token));
        }
        return false;
    }

    // Access 토큰에서 사용자 정보 가져오기
    public String getUserIdFromAccessToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", String.class);
    }

    // Refresh 토큰에서 사용자 정보 가져오기
    public String getUserIdFromRefreshToken(String token) {
        return Jwts.parser().verifyWith(refreshSecretKey).build().parseSignedClaims(token).getPayload().get("userId", String.class);
    }

    public String getUserRoleFromAccessToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(AUTHORIZATION_KEY, String.class);
    }

    public String getUserRoleFromRefreshToken(String token) {
        return Jwts.parser().verifyWith(refreshSecretKey).build().parseSignedClaims(token).getPayload().get(AUTHORIZATION_KEY, String.class);
    }

    public Boolean isNotExpiredAccessToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 Access 토큰입니다. 사용자 ID: {}", getUserIdFromAccessToken(token));
        }
        return false;
    }

    public Boolean isNotExpiredRefreshToken(String token) {
        try {
            Jwts.parser().verifyWith(refreshSecretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 Refresh 토큰입니다. 사용자 ID: {}", getUserIdFromRefreshToken(token));
        }
        return false;
    }
}
