package com.soa.onlinestorebackend.user.service;

import com.soa.onlinestorebackend.user.dto.request.UpdateUserRequest;
import com.soa.onlinestorebackend.common.response.PageResponse;
import com.soa.onlinestorebackend.user.dto.response.UserResponseDTO;
import com.soa.onlinestorebackend.security.config.security.CustomUserPrincipal;

import java.util.Set;

import org.springframework.data.domain.Pageable;

public interface IUserService {

    // UserResponseDTO create(UserRequestDTO userRequestDTO);

    // UserResponseDTO update(Long id, UserRequestDTO userRequestDTO);

    // boolean delete(Long id);

    // Optional<UserResponseDTO> getById(Long id);

    // List<UserResponseDTO> getAll();

    public UserResponseDTO getCurrentUser(CustomUserPrincipal principal);

    public UserResponseDTO updateProfile(CustomUserPrincipal principal, UpdateUserRequest request);
    
    public PageResponse<UserResponseDTO> getAllUsers(Pageable pageable);

    public PageResponse<UserResponseDTO> searchUsers(String search, Pageable pageable);

    public UserResponseDTO getUserById(Long id);

    public UserResponseDTO updateUserRoles(Long userId, Set<String> roleNames);

    public UserResponseDTO toggleUserStatus(Long userId);
}
