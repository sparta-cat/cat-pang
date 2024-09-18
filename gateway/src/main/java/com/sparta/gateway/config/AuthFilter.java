package com.sparta.gateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j(topic = "Auth Filter")
@Component
@RequiredArgsConstructor
public class AuthFilter implements GatewayFilter {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 요청에서 Authorization 헤더 가져오기
        HttpHeaders headers = exchange.getRequest().getHeaders();

        // 토큰이 없는 경우, 인증을 바로 실패 처리
        if (headers.getFirst(HttpHeaders.AUTHORIZATION) == null) {
            return Mono.error(new RuntimeException("Authorization header is missing"));
        }


        // WebClient를 통해 Auth 서버로 인증 요청 보내기
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:19092/api/v1/auth/authorize") // Auth 서버에 토큰 검증 요청
                .headers(httpHeaders -> httpHeaders.addAll(headers)) // 기존 헤더 전달
                .retrieve()
                .bodyToMono(UserDto.ForAddHeader.class)
                .flatMap(userInfo -> {
                    // UserDto.Info에서 userId와 role을 추출하고 헤더에 추가
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-User-ID", userInfo.userId())
                            .header("X-User-Role", userInfo.role())
                            .build();

                    // 헤더에 userId와 role을 추가한 새로운 요청으로 필터 체인 계속 진행
                    return chain.filter(exchange.mutate().request(request).build());
                })
                .doOnError(error -> log.error("Error during token validation: {}", error.getMessage()));
    }

}
