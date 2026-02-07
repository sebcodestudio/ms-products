package com.soa.onlinestorebackend.product.mapper;

import com.soa.onlinestorebackend.product.dto.response.ProductImgResponseDTO;
import com.soa.onlinestorebackend.product.entity.ProductImg;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImgMapper {

    ProductImgResponseDTO toResponseDTO(ProductImg productImg);

}
