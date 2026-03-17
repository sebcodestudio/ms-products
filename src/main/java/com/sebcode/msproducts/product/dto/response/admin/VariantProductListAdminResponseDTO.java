package com.sebcode.msproducts.product.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantProductListAdminResponseDTO {
    private Long id;
    private String sku;
    private String productName;

    // Precios
    private BigDecimal price;
    private BigDecimal finalPrice;
    private Integer discount;
    private Boolean hasActiveDiscount;

    // Stock
    private Integer stock;

    // Estado
    private Boolean state;
}
