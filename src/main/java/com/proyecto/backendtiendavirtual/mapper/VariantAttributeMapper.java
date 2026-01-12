package com.proyecto.backendtiendavirtual.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.proyecto.backendtiendavirtual.dto.response.VariantAttributeCardResponseDTO;
import com.proyecto.backendtiendavirtual.dto.response.VariantAttributeDetailResponseDTO;
import com.proyecto.backendtiendavirtual.entity.VariantAttribute;

@Mapper(componentModel = "spring", uses = { ProductImgMapper.class, AttributeValueMapper.class })
public interface VariantAttributeMapper {

    @Mapping(target = "id", source = "idVariantAttribute")
    VariantAttributeDetailResponseDTO toResponseDetailDTO(VariantAttribute variantAttribute);

    @Mapping(target = "id", source = "idVariantAttribute")
    VariantAttributeCardResponseDTO toResponseCardDTO(VariantAttribute variantAttribute);

}
