package com.proyecto.backendtiendavirtual.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.proyecto.backendtiendavirtual.dto.response.CategoryResponseDTO;
import com.proyecto.backendtiendavirtual.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", source = "idCategory")
    CategoryResponseDTO toResponseDTO(Category category);

}
