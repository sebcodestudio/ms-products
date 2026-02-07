package com.soa.onlinestorebackend.product.mapper;

import com.soa.onlinestorebackend.product.dto.request.VariantProductRequestDTO;
import com.soa.onlinestorebackend.product.dto.response.VariantProductCardResponseDTO;
import com.soa.onlinestorebackend.product.dto.response.VariantProductDetailResponseDTO;
import com.soa.onlinestorebackend.product.entity.VariantProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {VariantAttributeMapper.class})
public interface VariantProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discountUpdateUser", ignore = true)
    @Mapping(target = "discountUpdateDate", ignore = true)
    @Mapping(target = "deleteDate", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cartProducts", ignore = true)
    @Mapping(target = "variantAttributes", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    VariantProduct toEntity(VariantProductRequestDTO variantProductRequestDTO);

    VariantProductDetailResponseDTO toResponseDetailDTO(VariantProduct variantProduct);

    @Mapping(target = "score", source = "product.score")
    @Mapping(target = "description", source = "product.description")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "brand", source = "product.brand.name")
    VariantProductCardResponseDTO toResponseCardDTO(VariantProduct variantProduct);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discountUpdateUser", ignore = true)
    @Mapping(target = "discountUpdateDate", ignore = true)
    @Mapping(target = "deleteDate", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cartProducts", ignore = true)
    @Mapping(target = "variantAttributes", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    void updateEntityFromDTO(VariantProductRequestDTO variantProductRequestDTO, @MappingTarget VariantProduct variantProduct);

}
