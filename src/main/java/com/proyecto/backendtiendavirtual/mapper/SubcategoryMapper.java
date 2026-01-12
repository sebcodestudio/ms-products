package com.proyecto.backendtiendavirtual.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.proyecto.backendtiendavirtual.dto.response.SubcategoryCardResponseDTO;
import com.proyecto.backendtiendavirtual.dto.response.SubcategoryDetailResponseDTO;
import com.proyecto.backendtiendavirtual.entity.Subcategory;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {

    @Mapping(target = "id", source = "idSubcategory") 
    SubcategoryCardResponseDTO toResponseCardDTO(Subcategory subcategory);

    @Mapping(target = "id", source = "idSubcategory") 
    SubcategoryDetailResponseDTO toResponseDetailDTO(Subcategory subcategory);
    
}
