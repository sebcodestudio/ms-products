package com.sebcode.msproducts.product.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sebcode.msproducts.common.response.AuditableEntityResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "version", "sku", "productId", "productName", "originalPrice", "finalPrice", "discount"
        , "discountStartDate", "discountEndDate", "isDiscountActive", "discountUpdateUser", "discountUpdateDate", "stock", "soldCount"})
public class VariantProductDetailAdminResponseDTO extends AuditableEntityResponseDTO {
    private Long id;
    private Long version;
    private String sku;

    // Producto padre
    private Long productId;
    private String productName;

    // Precios
    private BigDecimal price;
    private BigDecimal originalPrice;
    private BigDecimal finalPrice;
    private Integer discount;
    private LocalDateTime discountStartDate;
    private LocalDateTime discountEndDate;
    private Boolean isDiscountActive;
    private Long discountUpdateUser;
    private LocalDateTime discountUpdateDate;

    // Stock
    private Integer stock;
    private Integer soldCount;
}