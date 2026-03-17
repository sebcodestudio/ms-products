package com.sebcode.msproducts.product.mapper;

import com.sebcode.msproducts.product.dto.request.VariantProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductDetailAdminResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductListAdminResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductDetailPublicResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductListPublicResponseDTO;
import com.sebcode.msproducts.product.entity.VariantProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {VariantAttributeMapper.class})
public interface VariantProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discountUpdateUser", ignore = true)
    @Mapping(target = "discountUpdateDate", ignore = true)
    @Mapping(target = "product", ignore = true)
//    @Mapping(target = "cartProducts", ignore = true)
    @Mapping(target = "variantAttributes", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    VariantProduct toEntity(VariantProductRequestDTO variantProductRequestDTO);

    VariantProductDetailAdminResponseDTO toDetailAdminResponseDTO(VariantProduct variantProduct);

    VariantProductListAdminResponseDTO toListAdminResponseDTO(VariantProduct variantProduct);

    VariantProductDetailPublicResponseDTO toDetailPublicResponseDTO(VariantProduct variantProduct);

    VariantProductListPublicResponseDTO toListPublicResponseDTO(VariantProduct variantProduct);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discountUpdateUser", ignore = true)
    @Mapping(target = "discountUpdateDate", ignore = true)
    @Mapping(target = "product", ignore = true)
//    @Mapping(target = "cartProducts", ignore = true)
    @Mapping(target = "variantAttributes", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "attributesMap", ignore = true)
    void updateEntityFromDTO(VariantProductRequestDTO variantProductRequestDTO, @MappingTarget VariantProduct variantProduct);

}
