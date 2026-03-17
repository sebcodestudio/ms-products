package com.sebcode.msproducts.product.mapper;

import com.sebcode.msproducts.product.dto.request.VariantAttributeRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantAttributeDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantAttributeListResponseDTO;
import com.sebcode.msproducts.product.entity.AttributeType;
import com.sebcode.msproducts.product.entity.AttributeValue;
import com.sebcode.msproducts.product.entity.VariantAttribute;
import com.sebcode.msproducts.product.entity.VariantProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ProductImgMapper.class, VariantAttributeMapper.class})
public interface VariantAttributeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variantProduct", source = "variantProductId", qualifiedByName = "mapVariantProduct")
    @Mapping(target = "attributeType", source = "attributeTypeId", qualifiedByName = "mapAttributeType")
    @Mapping(target = "attributeValue", source = "attributeValueId", qualifiedByName = "mapAttributeValue")
    VariantAttribute toEntity(VariantAttributeRequestDTO variantAttributeRequestDTO);

    @Mapping(target = "variantProductId", source = "variantProduct.id")
    @Mapping(target = "variantProductName", source = "variantProduct.name")
    @Mapping(target = "attributeTypeId", source = "attributeType.id")
    @Mapping(target = "attributeTypeName", source = "attributeType.name")
    @Mapping(target = "attributeValueId", source = "attributeValue.id")
    @Mapping(target = "attributeValueName", source = "attributeValue.value")
    VariantAttributeDetailResponseDTO toDetailResponseDTO(VariantAttribute variantAttribute);

    @Mapping(target = "variantProductName", source = "variantProduct.name")
    @Mapping(target = "attributeTypeName", source = "attributeType.name")
    @Mapping(target = "attributeValueName", source = "attributeValue.value")
    VariantAttributeListResponseDTO toListResponseDTO(VariantAttribute variantAttribute);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(VariantAttributeRequestDTO variantAttributeRequestDTO, @MappingTarget VariantAttribute variantAttribute);

    @Named("mapVariantProduct")
    default VariantProduct mapVariantProduct(Long id) {
        if (id == null) return null;
        VariantProduct variantProduct = new VariantProduct();
        variantProduct.setId(id);
        return variantProduct;
    }

    @Named("mapAttributeType")
    default AttributeType mapAttributeType(Long id) {
        if (id == null) return null;
        AttributeType attributeType = new AttributeType();
        attributeType.setId(id);
        return attributeType;
    }

    @Named("mapAttributeValue")
    default AttributeValue mapAttributeValue(Long id) {
        if (id == null) return null;
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(id);
        return attributeValue;
    }

}
