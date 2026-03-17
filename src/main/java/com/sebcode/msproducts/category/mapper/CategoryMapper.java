package com.sebcode.msproducts.category.mapper;

import com.sebcode.msproducts.category.dto.request.CategoryRequestDTO;
import com.sebcode.msproducts.category.dto.response.CategoryDetailResponseDTO;
import com.sebcode.msproducts.category.dto.response.CategoryListResponseDTO;
import com.sebcode.msproducts.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequestDTO categoryRequestDTO);

    CategoryDetailResponseDTO toDetailResponseDTO(Category category);

    CategoryListResponseDTO toListResponseDTO(Category category);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(CategoryRequestDTO categoryRequestDTO, @MappingTarget Category category);

}
