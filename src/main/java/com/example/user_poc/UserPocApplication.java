package com.example.user_poc;

import com.example.user_poc.config.JwtAuditorAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "jwtAuditorAware")
public class UserPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserPocApplication.class, args);
	}

}
