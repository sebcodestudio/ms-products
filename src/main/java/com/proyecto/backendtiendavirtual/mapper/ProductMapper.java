package com.proyecto.backendtiendavirtual.mapper;

import com.proyecto.backendtiendavirtual.dto.request.ProductRequestDTO;
// import com.proyecto.backendtiendavirtual.dto.response.ProductCardResponseDTO;
import com.proyecto.backendtiendavirtual.dto.response.ProductDetailResponseDTO;
import com.proyecto.backendtiendavirtual.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { VariantProductMapper.class, SubcategoryMapper.class, BrandMapper.class })
public interface ProductMapper {

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "soldCount", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "deleteDate", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "idProduct", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "variantProducts", ignore = true)
    Product toEntity(ProductRequestDTO productRequestDTO);

    @Mapping(target = "id", source = "idProduct")
    @Mapping(target = "brand", source = "brand.name")
    ProductDetailResponseDTO toResponseDetailDTO(Product product);

    // @Mapping(target = "price", expression = "java(product.getVariantProducts().isEmpty() ? null : product.getVariantProducts().get(0).getPrice())")
    // @Mapping(target = "priceDiscount", ignore = true)
    // @Mapping(target = "imageUrl", expression = "java(product.getVariantProducts().isEmpty() ? null : product.getVariantProducts().get(0).getProductImgs().get(0).getImageUrl())")
    // @Mapping(target = "discount", ignore = true)
    // @Mapping(target = "brand", source = "brand.name")
    // @Mapping(target = "id", source = "idProduct")
    // ProductCardResponseDTO toResponseCardDTO(Product product);

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "soldCount", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "deleteDate", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "idProduct", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "variantProducts", ignore = true)
    void updateEntityFromDTO(ProductRequestDTO productRequestDTO, @MappingTarget Product product);

}
