package com.catpang.core.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version : 1.0.0
 * @package : com.catpang.core.infrastructure.config
 * @name : OpenApiConfig.java
 * @date : 2024-09-18 오후 10시
 */
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    private static final String API_NAME = "CatPang";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "✨물류 관리 및 배송 시스템을 위한 MSA 기반 플랫폼 개발✨";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_NAME)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION));
    }
}
