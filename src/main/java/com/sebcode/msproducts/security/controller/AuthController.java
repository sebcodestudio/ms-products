package com.sebcode.msproducts.security.controller;

import com.sebcode.msproducts.security.config.security.CustomUserDetailsService;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import com.sebcode.msproducts.security.config.security.JwtTokenProvider;
import com.sebcode.msproducts.security.dto.response.AuthResponseDTO;
import com.sebcode.msproducts.security.dto.request.RefreshTokenRequestDTO;
import com.sebcode.msproducts.security.entity.TokenRecuperation;
import com.sebcode.msproducts.security.service.RefreshTokenService;
import com.sebcode.msproducts.user.dto.request.LoginRequestDTO;
import com.sebcode.msproducts.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates user and returns access + refresh tokens")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateToken(authentication);

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        var user = userRepository.findById(principal.getId())
                .orElseThrow();
        TokenRecuperation refreshToken = refreshTokenService.createRefreshToken(user);

        log.info("User logged in: {}", request.getEmail());

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(tokenProvider.getExpirationTime())
                .build());
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token", description = "Issues a new access token using a valid refresh token")
    public ResponseEntity<AuthResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        TokenRecuperation refreshToken = refreshTokenService.validateAndConsume(request.getRefreshToken());

        var user = refreshToken.getUser();
        CustomUserPrincipal principal = (CustomUserPrincipal) userDetailsService.loadUserByUsername(user.getEmail());

        String roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String newAccessToken = tokenProvider.generateTokenFromUsername(user.getEmail(), roles);
        TokenRecuperation newRefreshToken = refreshTokenService.createRefreshToken(user);

        log.info("Token refreshed for user: {}", user.getEmail());

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .expiresIn(tokenProvider.getExpirationTime())
                .build());
    }
}