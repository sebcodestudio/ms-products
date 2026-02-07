package com.soa.onlinestorebackend.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VariantProductRequestDTO {

    @NotBlank(message = "sku is required")
    private String sku;

    @Positive(message = "The price must be positive")
    private BigDecimal price;

    @PositiveOrZero(message = "The discount must be positive or zero")
    private Integer discount;

    @Positive(message = "The original price must be positive")
    private BigDecimal originalPrice;

    private LocalDateTime discountStartDate;

    private LocalDateTime discountEndDate;

    @PositiveOrZero(message = "The stock must be positive or zero")
    private int stock;

    @PositiveOrZero(message = "The sold count must be positive or zero")
    private int soldCount;

    private Boolean state;

    @Positive(message = "The productId must be positive")
    private Long productId;
//
//    @Positive(message = "The subcategoryId must be positive")
//    private Long subcategoryId;

}
