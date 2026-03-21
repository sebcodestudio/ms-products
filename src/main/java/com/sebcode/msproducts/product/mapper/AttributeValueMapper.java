package com.sebcode.msproducts.product.mapper;

import com.sebcode.msproducts.product.dto.request.AttributeValueRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeValueDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeValueListResponseDTO;
import com.sebcode.msproducts.product.entity.AttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AttributeTypeMapper.class})
public interface AttributeValueMapper {

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "variantProduct", source = "variantProductId", qualifiedByName = "mapVariantProduct")
//    @Mapping(target = "attributeValue", source = "attributeValueId", qualifiedByName = "mapAttributeValue")
    @Mapping(target = "attributeType", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    AttributeValue toEntity(AttributeValueRequestDTO attributeValueRequestDTO);

    @Mapping(target = "attributeTypeId", source = "attributeType.id")
    @Mapping(target = "attributeTypeName", source = "attributeType.name")
    AttributeValueDetailResponseDTO toDetailResponseDTO(AttributeValue attributeValue);

    //    @Mapping(target = "variantProductSku", source = "variantProduct.sku")
//    @Mapping(target = "attributeValueName", source = "attributeValue.value")
    @Mapping(target = "attributeTypeName", source = "attributeType.name")
    AttributeValueListResponseDTO toListResponseDTO(AttributeValue attributeValue);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "variantProduct", source = "variantProductId", qualifiedByName = "mapVariantProduct")
//    @Mapping(target = "attributeValue", source = "attributeValueId", qualifiedByName = "mapAttributeValue")
    @Mapping(target = "attributeType", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    void updateEntityFromDTO(AttributeValueRequestDTO attributeValueRequestDTO, @MappingTarget AttributeValue attributeValue);

//    @Named("mapVariantProduct")
//    default VariantProduct mapVariantProduct(Long id) {
//        if (id == null) return null;
//        VariantProduct variantProduct = new VariantProduct();
//        variantProduct.setId(id);
//        return variantProduct;
//    }
//
//    @Named("mapAttributeValue")
//    default AttributeValue mapAttributeValue(Long id) {
//        if (id == null) return null;
//        AttributeValue attributeValue = new AttributeValue();
//        attributeValue.setId(id);
//        return attributeValue;
//    }

}
