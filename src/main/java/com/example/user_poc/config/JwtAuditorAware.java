package com.example.user_poc.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org. springframework. security. core. Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("jwtAuditorAware")
public class JwtAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("SYSTEM");
        }

        return Optional.of(authentication.getName());
    }
}
