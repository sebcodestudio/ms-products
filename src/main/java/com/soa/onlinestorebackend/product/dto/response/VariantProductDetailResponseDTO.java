package com.soa.onlinestorebackend.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariantProductDetailResponseDTO {

    private Long id;
    private String sku;
    private Integer stock;
    private BigDecimal price;
    private Integer discount;
    private BigDecimal priceDiscount;
    private List<VariantAttributeDetailResponseDTO> variantAttributes;

}
