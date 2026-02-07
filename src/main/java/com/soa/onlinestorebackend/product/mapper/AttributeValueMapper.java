package com.soa.onlinestorebackend.product.mapper;

import com.soa.onlinestorebackend.product.dto.response.AttributeValueResponseDTO;
import com.soa.onlinestorebackend.product.entity.AttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeTypeMapper.class})
public interface AttributeValueMapper {

    @Mapping(target = "type", source = "attributeType.name")
    AttributeValueResponseDTO toResponseDTO(AttributeValue attributeValue);

}
