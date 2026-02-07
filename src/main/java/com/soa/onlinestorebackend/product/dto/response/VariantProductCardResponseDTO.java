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
public class VariantProductCardResponseDTO {

    private Long id;
    private String sku;

    //Datos producto
    private String name;
    private String description;
    private String brand;
    private float score;

    private BigDecimal price;
    private Integer discount;
    private BigDecimal priceDiscount;
    private List<VariantAttributeCardResponseDTO> variantAttributes;

}
