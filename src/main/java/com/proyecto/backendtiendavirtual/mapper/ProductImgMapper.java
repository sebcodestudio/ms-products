package com.proyecto.backendtiendavirtual.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.proyecto.backendtiendavirtual.dto.response.ProductImgResponseDTO;
import com.proyecto.backendtiendavirtual.entity.ProductImg;

@Mapper(componentModel = "spring")
public interface ProductImgMapper {

    @Mapping(target = "id", source = "idProductImg")
    ProductImgResponseDTO toResponseDTO(ProductImg productImg);

}
