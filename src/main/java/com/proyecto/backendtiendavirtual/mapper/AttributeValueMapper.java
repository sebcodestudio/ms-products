package com.proyecto.backendtiendavirtual.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.proyecto.backendtiendavirtual.dto.response.AttributeValueResponseDTO;
import com.proyecto.backendtiendavirtual.entity.AttributeValue;

@Mapper(componentModel = "spring" , uses = { AttributeTypeMapper.class})
public interface AttributeValueMapper {

    @Mapping(target = "id", source = "idAttributeValue")
    @Mapping(target = "type", source = "attributeType.name")
    AttributeValueResponseDTO toResponseDTO(AttributeValue attributeValue);

}
