package com.sebcode.msproducts.product.mapper;

import com.sebcode.msproducts.category.entity.Subcategory;
import com.sebcode.msproducts.product.dto.request.ProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductListResponseDTO;
import com.sebcode.msproducts.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id",              ignore = true)
    @Mapping(target = "score",           ignore = true)
    @Mapping(target = "brand",           ignore = true)
    @Mapping(target = "variantProducts", ignore = true)
    @Mapping(target = "subcategories",   ignore = true)
    @Mapping(target = "state",           ignore = true)
    @Mapping(target = "isDeleted",       ignore = true)
    @Mapping(target = "deleteAt",        ignore = true)
    @Mapping(target = "createdUser",     ignore = true)
    @Mapping(target = "createdAt",       ignore = true)
    @Mapping(target = "updateUser",      ignore = true)
    @Mapping(target = "updateAt",        ignore = true)
    Product toEntity(ProductRequestDTO productRequestDTO);

    @Mapping(target = "brandId",       source = "brand.id")
    @Mapping(target = "brand",         source = "brand.name")
    @Mapping(target = "subcategories", source = "subcategories", qualifiedByName = "toSubcategoryRefs")
    ProductDetailResponseDTO toDetailResponseDTO(Product product);

    @Mapping(target = "brand",         source = "brand.name")
    @Mapping(target = "subcategories", source = "subcategories", qualifiedByName = "toSubcategoryNames")
    ProductListResponseDTO toListResponseDTO(Product product);

    @Mapping(target = "id",              ignore = true)
    @Mapping(target = "score",           ignore = true)
    @Mapping(target = "brand",           ignore = true)
    @Mapping(target = "variantProducts", ignore = true)
    @Mapping(target = "subcategories",   ignore = true)
    @Mapping(target = "state",           ignore = true)
    @Mapping(target = "isDeleted",       ignore = true)
    @Mapping(target = "deleteAt",        ignore = true)
    @Mapping(target = "createdUser",     ignore = true)
    @Mapping(target = "createdAt",       ignore = true)
    @Mapping(target = "updateUser",      ignore = true)
    @Mapping(target = "updateAt",        ignore = true)
    void updateEntityFromDTO(ProductRequestDTO productRequestDTO, @MappingTarget Product product);

    @Named("toSubcategoryRefs")
    default List<ProductDetailResponseDTO.SubcategoryRefDTO> toSubcategoryRefs(List<Subcategory> subcategories) {
        if (subcategories == null) return Collections.emptyList();
        return subcategories.stream()
                .map(s -> ProductDetailResponseDTO.SubcategoryRefDTO.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .build())
                .toList();
    }

    @Named("toSubcategoryNames")
    default List<String> toSubcategoryNames(List<Subcategory> subcategories) {
        if (subcategories == null) return Collections.emptyList();
        return subcategories.stream()
                .map(Subcategory::getName)
                .toList();
    }
}