package kenayperez.useraccess2.controllers;

import jakarta.validation.Valid;
import kenayperez.useraccess2.dto.RegisterRequest;
import kenayperez.useraccess2.dto.RegisterResponse;
import kenayperez.useraccess2.dto.UserDto;
import kenayperez.useraccess2.entities.UserEntity;
import kenayperez.useraccess2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        UserEntity user = authService.register(request);
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
        RegisterResponse response = new RegisterResponse("success", "User successfully registered.", userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
