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
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    VariantProduct toEntity(VariantProductRequestDTO variantProductRequestDTO);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "isDiscountActive", expression = "java(variantProduct.isDiscountActive())")
    VariantProductDetailAdminResponseDTO toDetailAdminResponseDTO(VariantProduct variantProduct);

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "hasActiveDiscount", expression = "java(variantProduct.isDiscountActive())")
    VariantProductListAdminResponseDTO toListAdminResponseDTO(VariantProduct variantProduct);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productDescription", source = "product.description")
    @Mapping(target = "brandName", source = "product.brand.name")
    @Mapping(target = "productRating", source = "product.score")
    @Mapping(target = "hasActiveDiscount", expression = "java(variantProduct.isDiscountActive())")
    @Mapping(target = "discountPercentage", ignore = true)
//    @Mapping(target = "attributes", source = "brand.name")
//    @Mapping(target = "images", source = "brand.name")
//    @Mapping(target = "availableVariants", source = "brand.name")
//    @Mapping(target = "availableAttributes", source = "brand.name")
    VariantProductDetailPublicResponseDTO toDetailPublicResponseDTO(VariantProduct variantProduct);

    @Mapping(target = "productId", source = "product.id")
//    @Mapping(target = "discountUpdateUser", ignore = true)
    @Mapping(target = "hasDiscount", ignore = true)
    @Mapping(target = "inStock", ignore = true)
    @Mapping(target = "lowStock", ignore = true)
    @Mapping(target = "mainAttributes", ignore = true)
    @Mapping(target = "mainImageUrl", ignore = true)
    @Mapping(target = "isNew", ignore = true)
    @Mapping(target = "discountPercentage", ignore = true)
    @Mapping(target = "productRating", source = "product.score")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "brandName", source = "product.brand.name")
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
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    void updateEntityFromDTO(VariantProductRequestDTO variantProductRequestDTO, @MappingTarget VariantProduct variantProduct);

}
