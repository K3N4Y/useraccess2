package kenayperez.useraccess2.service;

import kenayperez.useraccess2.dto.*;
import kenayperez.useraccess2.dto.mapper.UsertoDTO;
import kenayperez.useraccess2.entities.UserEntity;
import kenayperez.useraccess2.repository.UserRepository;
import kenayperez.useraccess2.security.jwt.JwtTokenProvider;
import kenayperez.useraccess2.security.jwt.SecurityUtils;
import kenayperez.useraccess2.security.userdetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UsertoDTO usertoDTO;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private SecurityUtils securityUtils;

    AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UsernameNotFoundException("Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        UserDto userDto = usertoDTO.toUserDto(user);
        return new RegisterResponse("success", "User successfully registered.", userDto);
    }

    @Transactional(readOnly = true)
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        String jwt = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return new JwtResponse(
                jwt,
                refreshToken,
                "Bearer",
                securityUtils.getExpirationTimeInSeconds(),
                usertoDTO.toUserDto(user.getUser())
        );
    }
}
