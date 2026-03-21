package com.sebcode.msproducts.product.mapper;

import com.sebcode.msproducts.product.dto.request.BrandRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.BrandDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.BrandListResponseDTO;
import com.sebcode.msproducts.product.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    Brand toEntity(BrandRequestDTO brandRequestDTO);

    BrandDetailResponseDTO toDetailResponseDTO(Brand brand);

    BrandListResponseDTO toListResponseDTO(Brand brand);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateEntityFromDTO(BrandRequestDTO brandRequestDTO, @MappingTarget Brand brand);

}
