package com.catpang.user.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory // RedisTemplate과 같이 Redis와의 연결정보가 구성돼야 함.
    ) {
        // 사용자 데이터의 TTL을 Access Token과 동일하게 설정 (10분)
        RedisCacheConfiguration userDataCacheConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(10))
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json())
                );

        // Refresh Token의 TTL 설정 (7일)
        RedisCacheConfiguration refreshTokenCacheConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofDays(7))
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json())
                );

        return RedisCacheManager.builder(redisConnectionFactory)
                // 사용자 데이터 캐시 설정 (accessToken 생명 주기와 같음)
                .withCacheConfiguration("userDetails", userDataCacheConfig)
                // RefreshToken 캐시 설정 (refreshToken 생명 주기와 같음)
                .withCacheConfiguration("refreshToken", refreshTokenCacheConfig)
                .build();

    }
}
