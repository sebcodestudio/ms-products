package com.proyecto.backendtiendavirtual.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.proyecto.backendtiendavirtual.dto.response.VariantProductCardResponseDTO;
import com.proyecto.backendtiendavirtual.dto.response.VariantProductDetailResponseDTO;
import com.proyecto.backendtiendavirtual.entity.VariantProduct;

@Mapper(componentModel = "spring", uses = { VariantAttributeMapper.class })
public interface VariantProductMapper {

    @Mapping(target = "id", source = "idVariantProduct")
    VariantProductDetailResponseDTO toResponseDetailDTO(VariantProduct variantProduct);

    @Mapping(target = "id", source = "idVariantProduct")
    @Mapping(target = "score", source = "product.score")
    @Mapping(target = "description", source = "product.description")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "brand", source = "product.brand.name")
    VariantProductCardResponseDTO toResponseCardDTO(VariantProduct variantProduct);

}
