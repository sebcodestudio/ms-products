package com.soa.onlinestorebackend.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDTO {

    private Long id;
    private String name;
    private String description;
    private float score;
    private String brand;
    private List<VariantProductDetailResponseDTO> variantProducts;

}
