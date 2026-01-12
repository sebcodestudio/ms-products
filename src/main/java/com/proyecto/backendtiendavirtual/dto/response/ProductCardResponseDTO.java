package com.proyecto.backendtiendavirtual.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
