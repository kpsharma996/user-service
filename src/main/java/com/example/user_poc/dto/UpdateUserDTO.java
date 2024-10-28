package com.example.user_poc.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserDTO {

    private String phoneNumber;

    @Email(message = "Valid email is required")
    private String email;
}
