package com.soa.onlinestorebackend.category.mapper;

import com.soa.onlinestorebackend.category.dto.response.CategoryResponseDTO;
import com.soa.onlinestorebackend.category.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDTO toResponseDTO(Category category);

}
