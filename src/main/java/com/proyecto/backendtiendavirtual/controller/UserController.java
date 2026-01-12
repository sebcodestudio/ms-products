package com.proyecto.backendtiendavirtual.controller;

import com.proyecto.backendtiendavirtual.dto.request.UpdateUserRequest;
import com.proyecto.backendtiendavirtual.dto.response.PageResponse;
import com.proyecto.backendtiendavirtual.dto.response.UserResponseDTO;
import com.proyecto.backendtiendavirtual.security.CustomUserPrincipal;
import com.proyecto.backendtiendavirtual.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final IUserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<UserResponseDTO> getCurrentUser(
        @AuthenticationPrincipal CustomUserPrincipal principal) {
            
        UserResponseDTO response = userService.getCurrentUser(principal);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    @Operation(summary = "GEt current user profile")
    public ResponseEntity<UserResponseDTO> getCurrentUser(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @Valid @RequestBody UpdateUserRequest request) {
            UserResponseDTO response = userService.updateProfile(principal, request);
            return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<PageResponse<UserResponseDTO>> getAllUsers(
        @PageableDefault(size = 20) Pageable pageable) {
            PageResponse<UserResponseDTO> response = userService.getAllUsers(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Search users (Admin only)")
    public ResponseEntity<PageResponse<UserResponseDTO>> searchUsers(
        @RequestParam String search,
        @PageableDefault(size = 20) Pageable pageable) {
            PageResponse<UserResponseDTO> response = userService.searchUsers(search, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID (Admin only)")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

}
