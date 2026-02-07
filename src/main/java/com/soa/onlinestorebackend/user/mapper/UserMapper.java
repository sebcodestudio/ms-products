package com.soa.onlinestorebackend.user.mapper;

import com.soa.onlinestorebackend.user.dto.request.UserRequestDTO;
import com.soa.onlinestorebackend.user.dto.response.UserResponseDTO;
import com.soa.onlinestorebackend.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(target = "age", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "customerOrders", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "tokenRecuperations", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    @Mapping(target = "id", ignore = true)
    UserResponseDTO toResponseDTO(User user);

//    @Mapping(target = "age", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "customerOrders", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "tokenRecuperations", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    void updateFromEntityToDTO(UserRequestDTO userRequestDTO, @MappingTarget User user);

}
