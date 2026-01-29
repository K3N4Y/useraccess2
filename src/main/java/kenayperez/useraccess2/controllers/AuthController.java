package kenayperez.useraccess2.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kenayperez.useraccess2.dto.*;
import kenayperez.useraccess2.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        URI location = URI.create("/api/auth/register");
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticate(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        JwtResponse jwtResponse = authService.login(request, httpRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest) {
        RefreshTokenResponse response = authService.refreshAccessToken(
                request.refreshToken(),
                httpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
