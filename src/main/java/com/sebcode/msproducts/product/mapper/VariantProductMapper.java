package com.sebcode.msproducts.product.mapper;

import com.sebcode.msproducts.product.dto.request.VariantProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductDetailAdminResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductListAdminResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductDetailPublicResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductListPublicResponseDTO;
import com.sebcode.msproducts.product.dto.response.shared.ProductImageResponseDTO;
import com.sebcode.msproducts.product.dto.response.shared.VariantAttributeResponseDTO;
import com.sebcode.msproducts.product.entity.ProductImage;
import com.sebcode.msproducts.product.entity.VariantAttribute;
import com.sebcode.msproducts.product.entity.VariantProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {VariantAttributeMapper.class})
public interface VariantProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discountUpdateUser", ignore = true)
    @Mapping(target = "discountUpdateDate", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "variantAttributes", ignore = true)
    @Mapping(target = "version", ignore = true)
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
    @Mapping(target = "finalPrice", expression = "java(variantProduct.getFinalPrice())")
    @Mapping(target = "available", expression = "java(variantProduct.isAvailable())")
    @Mapping(target = "discountPercentage", source = "discount")
    @Mapping(target = "images", expression = "java(mapImages(variantProduct.getResolvedImages()))")
    @Mapping(target = "attributes", expression = "java(variantProduct.getAttributesMap())")
    @Mapping(target = "variantAttributes", source = "variantAttributes", qualifiedByName = "mapVariantAttributes")
    @Mapping(target = "availableVariants", ignore = true)
    @Mapping(target = "availableAttributes", ignore = true)
    VariantProductDetailPublicResponseDTO toDetailPublicResponseDTO(VariantProduct variantProduct);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "brandName", source = "product.brand.name")
    @Mapping(target = "productRating", source = "product.score")
    @Mapping(target = "finalPrice", expression = "java(variantProduct.getFinalPrice())")
    @Mapping(target = "hasDiscount", expression = "java(variantProduct.isDiscountActive())")
    @Mapping(target = "discountPercentage", source = "discount")
    @Mapping(target = "inStock", expression = "java(variantProduct.getStock() != null && variantProduct.getStock() > 0 && Boolean.TRUE.equals(variantProduct.getState()))")
    @Mapping(target = "lowStock", expression = "java(variantProduct.getStock() != null && variantProduct.getStock() > 0 && variantProduct.getStock() < 10)")
    @Mapping(target = "mainAttributes", expression = "java(variantProduct.getAttributesMap())")
    // Foto "general" del producto (no cambia según el color de esta variante) — para catálogo/listado.
    @Mapping(target = "mainImageUrl", expression = "java(variantProduct.getProduct() != null ? variantProduct.getProduct().getMainImageUrl() : null)")
    @Mapping(target = "isNew", expression = "java(variantProduct.getCreatedAt() != null && variantProduct.getCreatedAt().isAfter(java.time.LocalDateTime.now().minusDays(30)))")
    VariantProductListPublicResponseDTO toListPublicResponseDTO(VariantProduct variantProduct);

    @Named("mapImages")
    default List<ProductImageResponseDTO> mapImages(List<ProductImage> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .sorted(Comparator.comparingInt(img -> img.getImageOrder() != null ? img.getImageOrder() : 0))
                .map(img -> ProductImageResponseDTO.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .altText(img.getAltText())
                        .isMain(img.getIsMain())
                        .imageType(img.getImageType())
                        .imageOrder(img.getImageOrder())
                        .build())
                .collect(Collectors.toList());
    }

    @Named("mapVariantAttributes")
    default List<VariantAttributeResponseDTO> mapVariantAttributes(List<VariantAttribute> attrs) {
        if (attrs == null) return Collections.emptyList();
        return attrs.stream()
                .map(attr -> VariantAttributeResponseDTO.builder()
                        .id(attr.getId())
                        .attributeName(attr.getAttributeType() != null ? attr.getAttributeType().getName() : null)
                        .attributeValue(attr.getAttributeValue() != null ? attr.getAttributeValue().getValue() : null)
                        .attributeTypeId(attr.getAttributeType() != null ? attr.getAttributeType().getId() : null)
                        .attributeValueId(attr.getAttributeValue() != null ? attr.getAttributeValue().getId() : null)
                        .isVisual(attr.getAttributeType() != null ? attr.getAttributeType().getIsVisual() : false)
                        .displayOrder(attr.getAttributeType() != null ? attr.getAttributeType().getDisplayOrder() : 0)
                        .build())
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discountUpdateUser", ignore = true)
    @Mapping(target = "discountUpdateDate", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "variantAttributes", ignore = true)
    @Mapping(target = "version", ignore = true)
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
