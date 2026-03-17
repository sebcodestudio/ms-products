package com.sebcode.msproducts.product.mapper;

import com.sebcode.msproducts.product.dto.request.ProductImageRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductImageDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductImageListResponseDTO;
import com.sebcode.msproducts.product.entity.AttributeType;
import com.sebcode.msproducts.product.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attributeType", source = "attributeTypeId", qualifiedByName = "mapAttributeType")
    ProductImage toEntity(ProductImageRequestDTO productImageRequestDTO);

    @Mapping(target = "attributeTypeId", source = "attributeType.id")
    @Mapping(target = "attributeTypeName", source = "attributeType.name")
    ProductImageDetailResponseDTO toDetailResponseDTO(ProductImage productImage);

    @Mapping(target = "attributeTypeName", source = "attributeType.name")
    ProductImageListResponseDTO toListResponseDTO(ProductImage productImage);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ProductImageRequestDTO productImageRequestDTO, @MappingTarget ProductImage productImage);

    @Named("mapAttributeType")
    default AttributeType mapAttributeType(Long id) {
        if (id == null) return null;
        AttributeType attributeType = new AttributeType();
        attributeType.setId(id);
        return attributeType;
    }
}
