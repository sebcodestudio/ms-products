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
//    @Mapping(target = "attributeType", source = "attributeTypeId", qualifiedByName = "mapAttributeType")
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "attributeValue", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    ProductImage toEntity(ProductImageRequestDTO productImageRequestDTO);

    @Mapping(target = "attributeValueId", source = "attributeValue.id")
    @Mapping(target = "attributeValueName", source = "attributeValue.value")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    ProductImageDetailResponseDTO toDetailResponseDTO(ProductImage productImage);

    @Mapping(target = "attributeValueName", source = "attributeValue.value")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    ProductImageListResponseDTO toListResponseDTO(ProductImage productImage);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "attributeValue", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    void updateEntityFromDTO(ProductImageRequestDTO productImageRequestDTO, @MappingTarget ProductImage productImage);

//    @Named("mapAttributeType")
//    default AttributeType mapAttributeType(Long id) {
//        if (id == null) return null;
//        AttributeType attributeType = new AttributeType();
//        attributeType.setId(id);
//        return attributeType;
//    }
}
