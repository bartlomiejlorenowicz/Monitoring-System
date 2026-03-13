package com.platform.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(

        @Email
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
