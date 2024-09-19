package com.catpang.core.infrastructure;

import com.catpang.core.application.service.EntityMapper;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return Optional.empty();
        }

        // 사용자 ID를 반환
        return Optional.of(EntityMapper.getUserId(((UserDetails) (authentication.getPrincipal())).getUsername()));
    }
}