package kenayperez.useraccess2.service;

import jakarta.servlet.http.HttpServletRequest;
import kenayperez.useraccess2.dto.*;
import kenayperez.useraccess2.dto.mapper.UsertoDTO;
import kenayperez.useraccess2.entities.UserEntity;
import kenayperez.useraccess2.repository.UserRepository;
import kenayperez.useraccess2.security.jwt.JwtTokenProvider;
import kenayperez.useraccess2.security.jwt.SecurityUtils;
import kenayperez.useraccess2.security.userdetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        UserEntity user = new UserEntity();

        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(request.getEmail());
        }

        UserDto userDto = usertoDTO.toUserDto(user);

        return new RegisterResponse("success", "User successfully registered.", userDto);
    }

    @Transactional
    public JwtResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        String jwt = jwtTokenProvider.generateToken(authentication);
        // Use RefreshTokenService to create a secure, hashed refresh token
        String refreshToken = refreshTokenService.createRefreshToken(authentication.getName(), httpRequest);

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return new JwtResponse(
                jwt,
                refreshToken,
                "Bearer",
                securityUtils.getExpirationTimeInSeconds(),
                usertoDTO.toUserDto(user.getUser()));
    }

    @Transactional
    public RefreshTokenResponse refreshAccessToken(String refreshToken, HttpServletRequest request) {
        // Validate the refresh token and get the user
        UserEntity user = refreshTokenService.validateRefreshToken(refreshToken);

        // Create authentication object for token generation
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                user.getRoles().stream()
                        .map(role -> (org.springframework.security.core.GrantedAuthority) () -> role)
                        .toList());

        // Generate new access token
        String newAccessToken = jwtTokenProvider.generateToken(authentication);

        // Rotate refresh token (optional: you can keep the same token or rotate it)
        String newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken, request);

        return new RefreshTokenResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                securityUtils.getExpirationTimeInSeconds());
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.revokeRefreshToken(refreshToken);
        }
    }
}
