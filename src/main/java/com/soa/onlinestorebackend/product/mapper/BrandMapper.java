package com.soa.onlinestorebackend.product.mapper;

import com.soa.onlinestorebackend.product.dto.response.BrandResponseDTO;
import com.soa.onlinestorebackend.product.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandResponseDTO toResponseDTO(Brand brand);

}
