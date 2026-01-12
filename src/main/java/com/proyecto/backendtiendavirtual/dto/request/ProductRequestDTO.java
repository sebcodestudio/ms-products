package com.proyecto.backendtiendavirtual.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String detail;

    @Positive(message = "The price must be positive")
    private BigDecimal price;

    @Positive(message = "The id must be positive")
    private Long idCategory;

    @Positive(message = "The stock must be positive")
    private int stock;
    
}
