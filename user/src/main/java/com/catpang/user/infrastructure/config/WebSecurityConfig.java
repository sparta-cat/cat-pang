package com.catpang.user.infrastructure.config;

import com.catpang.core.infrastructure.config.GlobalSecurityConfig;
import com.catpang.user.application.service.TokenService;
import com.catpang.user.infrastructure.jwt.JwtUtil;
import com.catpang.user.infrastructure.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ComponentScan("com.catpang.core.infrastructure.config")
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final TokenService refreshTokenService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter filter = new LoginFilter(jwtUtil, refreshTokenService);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/login", "POST"));
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, GlobalSecurityConfig globalSecurityConfig) throws Exception {
        SecurityFilterChain coreSecurityFilterChain = globalSecurityConfig.coreSecurityFilterChain(http);
        // 필터 추가
        http.addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class);
        return coreSecurityFilterChain;
    }
}
