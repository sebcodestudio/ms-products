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
    @Mapping(target = "subcategory", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    Category toEntity(CategoryRequestDTO categoryRequestDTO);

    CategoryDetailResponseDTO toDetailResponseDTO(Category category);

    CategoryListResponseDTO toListResponseDTO(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subcategory", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    void updateEntityFromDTO(CategoryRequestDTO categoryRequestDTO, @MappingTarget Category category);

}
