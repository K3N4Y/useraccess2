package kenayperez.useraccess2.controllers;

import jakarta.validation.Valid;
import kenayperez.useraccess2.dto.JwtResponse;
import kenayperez.useraccess2.dto.LoginRequest;
import kenayperez.useraccess2.dto.RegisterRequest;
import kenayperez.useraccess2.dto.RegisterResponse;
import kenayperez.useraccess2.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // TODO:fix this endpoint
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest request) {
        JwtResponse jwtResponse = authService.login(request);
        return ResponseEntity.ok(jwtResponse);
    }
}
