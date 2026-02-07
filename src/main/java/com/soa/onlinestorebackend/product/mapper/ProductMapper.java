package com.soa.onlinestorebackend.product.mapper;

import com.soa.onlinestorebackend.category.mapper.SubcategoryMapper;
import com.soa.onlinestorebackend.product.dto.request.ProductRequestDTO;
import com.soa.onlinestorebackend.product.dto.response.ProductDetailResponseDTO;
import com.soa.onlinestorebackend.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {VariantProductMapper.class, SubcategoryMapper.class, BrandMapper.class})
public interface ProductMapper {

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "deleteDate", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "variantProducts", ignore = true)
    Product toEntity(ProductRequestDTO productRequestDTO);

    @Mapping(target = "brand", source = "brand.name")
    ProductDetailResponseDTO toResponseDetailDTO(Product product);

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "deleteDate", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "variantProducts", ignore = true)
    void updateEntityFromDTO(ProductRequestDTO productRequestDTO, @MappingTarget Product product);

}
