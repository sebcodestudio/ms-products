package com.soa.onlinestorebackend.product.mapper;

import com.soa.onlinestorebackend.product.dto.response.VariantAttributeCardResponseDTO;
import com.soa.onlinestorebackend.product.dto.response.VariantAttributeDetailResponseDTO;
import com.soa.onlinestorebackend.product.entity.VariantAttribute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductImgMapper.class, AttributeValueMapper.class})
public interface VariantAttributeMapper {

    VariantAttributeDetailResponseDTO toResponseDetailDTO(VariantAttribute variantAttribute);

    VariantAttributeCardResponseDTO toResponseCardDTO(VariantAttribute variantAttribute);

}
