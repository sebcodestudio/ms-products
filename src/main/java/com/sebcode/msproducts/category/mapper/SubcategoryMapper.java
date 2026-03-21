package com.sebcode.msproducts.category.mapper;

import com.sebcode.msproducts.category.dto.request.SubcategoryRequestDTO;
import com.sebcode.msproducts.category.dto.response.SubcategoryDetailResponseDTO;
import com.sebcode.msproducts.category.dto.response.SubcategoryListResponseDTO;
import com.sebcode.msproducts.category.entity.Category;
import com.sebcode.msproducts.category.entity.Subcategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "products", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    Subcategory toEntity(SubcategoryRequestDTO subcategoryRequestDTO);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    SubcategoryDetailResponseDTO toDetailResponseDTO(Subcategory subcategory);

    @Mapping(target = "categoryId", source = "category.id")
//    @Mapping(target = "categoryName", source = "category.name")
    SubcategoryListResponseDTO toListResponseDTO(Subcategory subcategory);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "products", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    void updateEntityFromDTO(SubcategoryRequestDTO subcategoryRequestDTO, @MappingTarget Subcategory subcategory);

    @Named("mapCategory")
    default Category mapCategory(Long id) {
        if (id == null) return null;
        Category category = new Category();
        category.setId(id);
        return category;
    }

}
