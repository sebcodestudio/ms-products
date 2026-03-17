package com.sebcode.msproducts.product.mapper;

import com.sebcode.msproducts.product.dto.request.AttributeValueRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeValueDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeValueListResponseDTO;
import com.sebcode.msproducts.product.entity.AttributeValue;
import com.sebcode.msproducts.product.entity.VariantProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {AttributeTypeMapper.class})
public interface AttributeValueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variantProduct", source = "variantProductId", qualifiedByName = "mapVariantProduct")
    @Mapping(target = "attributeValue", source = "attributeValueId", qualifiedByName = "mapAttributeValue")
    AttributeValue toEntity(AttributeValueRequestDTO attributeValueRequestDTO);

    @Mapping(target = "attributeTypeId", source = "attributeType.id")
    @Mapping(target = "attributeTypeName", source = "attributeType.name")
    AttributeValueDetailResponseDTO toDetailResponseDTO(AttributeValue attributeValue);

    @Mapping(target = "variantProductSku", source = "variantProduct.sku")
    @Mapping(target = "attributeValueName", source = "attributeValue.value")
    AttributeValueListResponseDTO toListResponseDTO(AttributeValue attributeValue);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variantProduct", source = "variantProductId", qualifiedByName = "mapVariantProduct")
    @Mapping(target = "attributeValue", source = "attributeValueId", qualifiedByName = "mapAttributeValue")
    void updateEntityFromDTO(AttributeValueRequestDTO attributeValueRequestDTO, @MappingTarget AttributeValue attributeValue);

    @Named("mapVariantProduct")
    default VariantProduct mapVariantProduct(Long id) {
        if (id == null) return null;
        VariantProduct variantProduct = new VariantProduct();
        variantProduct.setId(id);
        return variantProduct;
    }

    @Named("mapAttributeValue")
    default AttributeValue mapAttributeValue(Long id) {
        if (id == null) return null;
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(id);
        return attributeValue;
    }

}
