package com.sebcode.msproducts.product.mapper;

import com.sebcode.msproducts.product.dto.request.ProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductListResponseDTO;
import com.sebcode.msproducts.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {VariantProductMapper.class, BrandMapper.class})
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variantProducts", ignore = true)
    Product toEntity(ProductRequestDTO productRequestDTO);

    @Mapping(target = "brand", source = "brand.name")
    ProductDetailResponseDTO toResponseDetailDTO(Product product);

    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "brand", source = "brand.name")
    ProductDetailResponseDTO toDetailResponseDTO(Product product);

    @Mapping(target = "brand", source = "brand.name")
    ProductListResponseDTO toListResponseDTO(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variantProducts", ignore = true)
    void updateEntityFromDTO(ProductRequestDTO productRequestDTO, @MappingTarget Product product);

}
