package com.sparta.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {

    private final WebClient.Builder webClientBuilder;

    public GatewayConfig(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder, AuthFilter authFilter) {

        return builder.routes()
                .route("auth", r -> r.path("/api/v1/auth/**")
                        .uri("lb://USER")
                )
                .route("user", r -> r.path("/api/v1/users", "/api/v1/users/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://USER")
                )
                .route("order", r -> r.path("/api/v1/orders", "/api/v1/orders/**", "/api/v1/deliveries", "/api/v1/deliveries/**", "/api/v1/delivery-logs")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://ORDER-SERVICE")
                )
                .route("company", r -> r.path("/api/v1/companies", "/api/v1/companies/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://COMPANY-SERVICE")
                )
                .route("hub", r -> r.path("/api/v1/hubs", "/api/v1/hubs/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://HUB-SERVICE")
                )
                .route("hub-product", r -> r.path("/api/v1/hub-products", "/api/v1/hub-products/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://HUB-PRODUCT-SERVICE")
                )
                .route("address", r -> r.path("/api/v1/addresses", "/api/v1/addresses/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://ADDRESS-SERVICE")
                )
                .build();
    }
}
