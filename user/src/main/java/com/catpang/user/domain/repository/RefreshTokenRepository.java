package com.catpang.user.domain.repository;

import com.catpang.user.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    boolean existsByRefreshToken(String refreshToken);
}
