package com.soa.onlinestorebackend.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCardResponseDTO {

    private Long id;
    private String name;
    private String description;
    private float score;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal priceDiscount;
    private String brand;
    private String imageUrl;

}
