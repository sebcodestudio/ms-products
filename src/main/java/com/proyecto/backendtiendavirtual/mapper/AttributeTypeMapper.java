package com.proyecto.backendtiendavirtual.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.proyecto.backendtiendavirtual.dto.response.AttributeTypeResponseDTO;
import com.proyecto.backendtiendavirtual.entity.AttributeType;

@Mapper(componentModel = "spring")
public interface AttributeTypeMapper {

    @Mapping(target = "id", source = "idAttributeType")
    AttributeTypeResponseDTO toResponseDTO(AttributeType attributeType);

}
