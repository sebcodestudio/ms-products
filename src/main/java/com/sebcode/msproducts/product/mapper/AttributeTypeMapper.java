package com.sebcode.msproducts.product.mapper;

import com.sebcode.msproducts.product.dto.request.AttributeTypeRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeTypeDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeTypeListResponseDTO;
import com.sebcode.msproducts.product.entity.AttributeType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AttributeTypeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attributeValues", ignore = true)
    AttributeType toEntity(AttributeTypeRequestDTO attributeTypeRequestDTO);

    AttributeTypeDetailResponseDTO toDetailResponseDTO(AttributeType attributeType);

    AttributeTypeListResponseDTO toListResponseDTO(AttributeType attributeType);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attributeValues", ignore = true)
    void updateEntityFromDTO(AttributeTypeRequestDTO attributeTypeRequestDTO, @MappingTarget AttributeType attributeType);

}
