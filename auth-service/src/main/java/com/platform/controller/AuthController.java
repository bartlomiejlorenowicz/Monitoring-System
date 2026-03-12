package com.platform.controller;

import com.platform.dto.request.LoginRequest;
import com.platform.dto.response.TokenResponse;
import com.platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/secure")
    @PreAuthorize("hasRole('USER')")
    public String secureEndpoint() {
        return "secure";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "admin";
    }

    @GetMapping("/me")
    public Map<String, Object> me(Jwt jwt) {

        String username = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");

        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        return Map.of(
                "username", username,
                "email", email,
                "roles", realmAccess
        );
    }
}
