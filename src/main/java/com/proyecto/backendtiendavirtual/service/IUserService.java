package com.proyecto.backendtiendavirtual.service;

import com.proyecto.backendtiendavirtual.dto.request.UpdateUserRequest;
import com.proyecto.backendtiendavirtual.dto.response.PageResponse;
import com.proyecto.backendtiendavirtual.dto.response.UserResponseDTO;
import com.proyecto.backendtiendavirtual.security.CustomUserPrincipal;

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
