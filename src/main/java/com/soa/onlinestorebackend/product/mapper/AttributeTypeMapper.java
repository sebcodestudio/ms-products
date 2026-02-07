package com.soa.onlinestorebackend.product.mapper;

import com.soa.onlinestorebackend.product.dto.response.AttributeTypeResponseDTO;
import com.soa.onlinestorebackend.product.entity.AttributeType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttributeTypeMapper {

    AttributeTypeResponseDTO toResponseDTO(AttributeType attributeType);

}
