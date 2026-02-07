package com.soa.onlinestorebackend.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantAttributeDetailResponseDTO {

    private Long id;
    private List<AttributeValueResponseDTO> attributeValues;
    private List<ProductImgResponseDTO> productImgs;

}
