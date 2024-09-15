package com.catpang.core.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import com.catpang.core.infrastructure.AuditorAwareImpl;

@Configuration
public class AuditorConfig {

	@Bean
	public AuditorAware<Long> auditorProvider() {
		return new AuditorAwareImpl();
	}
}