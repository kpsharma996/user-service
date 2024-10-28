package com.example.user_poc.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingHelper {

    public void logUserAction(String action, String entity, String attributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "SYSTEM";
        log.info("User {} performed action :{} on entity :{} attributes:{}", username, action, entity, attributes);
    }
}
