package com.soa.onlinestorebackend.category.mapper;

import com.soa.onlinestorebackend.category.dto.response.SubcategoryCardResponseDTO;
import com.soa.onlinestorebackend.category.entity.Subcategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {

    SubcategoryCardResponseDTO toResponseCardDTO(Subcategory subcategory);

}
