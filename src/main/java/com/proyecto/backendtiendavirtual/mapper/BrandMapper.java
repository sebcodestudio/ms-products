package com.proyecto.backendtiendavirtual.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.proyecto.backendtiendavirtual.dto.response.BrandResponseDTO;
import com.proyecto.backendtiendavirtual.entity.Brand;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "id", source = "idBrand")
    BrandResponseDTO toResponseDTO(Brand brand);

}
