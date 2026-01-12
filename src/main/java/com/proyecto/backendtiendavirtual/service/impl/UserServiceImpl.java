package com.proyecto.backendtiendavirtual.service.impl;

import com.proyecto.backendtiendavirtual.dto.request.UpdateUserRequest;
// import com.proyecto.backendtiendavirtual.dto.request.UserRequestDTO;
import com.proyecto.backendtiendavirtual.dto.response.PageResponse;
import com.proyecto.backendtiendavirtual.dto.response.UserResponseDTO;
import com.proyecto.backendtiendavirtual.entity.User;
// import com.proyecto.backendtiendavirtual.exception.NotFoundException;
import com.proyecto.backendtiendavirtual.mapper.UserMapper;
import com.proyecto.backendtiendavirtual.repository.RoleRepository;
import com.proyecto.backendtiendavirtual.repository.UserRepository;
import com.proyecto.backendtiendavirtual.security.CustomUserPrincipal;
import com.proyecto.backendtiendavirtual.service.IUserService;
import com.proyecto.backendtiendavirtual.exception.BadRequestException;
import com.proyecto.backendtiendavirtual.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import java.util.List;
// import java.util.Optional;
import java.util.Set;

// import static java.util.stream.Collectors.toList;

import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getCurrentUser(CustomUserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateProfile(CustomUserPrincipal principal, UpdateUserRequest request) {
        User user = userRepository.findById(principal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());

        if(request.getCurrentPassword() != null && request.getNewPassword() != null) {
            if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new BadRequestException("Current password is incorrect");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        user = userRepository.save(user);
        log.info("User profile update: {}", user.getEmail());

        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponseDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserResponseDTO> userResponses = users.map(userMapper::toResponseDTO);
        return PageResponse.of(userResponses);
    }

    @Override
    public PageResponse<UserResponseDTO> searchUsers(String search, Pageable pageable) {
        Page<User> users = userRepository.searchUsers(search, pageable);
        Page<UserResponseDTO> userResponses = users.map(userMapper::toResponseDTO);
        return PageResponse.of(userResponses);
    }
    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponseDTO(user);
    }
    @Override
    public UserResponseDTO updateUserRoles(Long userId, Set<String> roleNames) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Set<com.proyecto.backendtiendavirtual.entity.Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new BadRequestException("Role not found: " + roleName));
            roles.add(role);
        }

        user.setRoles(roles);
        user = userRepository.save(user);

        log.info("Update roles for user {}: {}", user.getEmail());
        return userMapper.toResponseDTO(user);
    }
    @Override
    public UserResponseDTO toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setActive(!user.getActive());
        user = userRepository.save(user);

        log.info("User {} status changed to: {}", user.getEmail(), user.getActive());

        return userMapper.toResponseDTO(user);
    }

    // @Override
    // public UserResponseDTO create(UserRequestDTO userRequestDTO) {
    //     User user = userMapper.toEntity(userRequestDTO);
    //     User saved = userRepository.save(user);
    //     return userMapper.toResponseDTO(saved);
    // }

    // @Override
    // public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
    //     User user = userRepository.findById(id)
    //             .orElseThrow(() -> new NotFoundException("User with ID "+id+" not found"));
    //     userMapper.updateFromEntityToDTO(userRequestDTO, user);
    //     User updated = userRepository.save(user);
    //     return userMapper.toResponseDTO(updated);
    // }

    // @Override
    // public boolean delete(Long id) {
    //     if(!exist(id)) return false;
    //     userRepository.deleteById(id);
    //     return true;
    // }

    // @Override
    // public Optional<UserResponseDTO> getById(Long id) {
    //     return userRepository.findById(id).map(userMapper::toResponseDTO);
    // }

    // @Override
    // public List<UserResponseDTO> getAll() {
    //     return userRepository.findAll()
    //             .stream()
    //             .map(userMapper::toResponseDTO)
    //             .collect(toList());
    // }

    // private boolean exist(Long id) {
    //     return userRepository.existsById(id);
    // }
    
}