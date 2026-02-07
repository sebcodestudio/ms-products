package com.soa.onlinestorebackend.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private Boolean state;

    private Long brandId;

    private List<Long> subcategoryId;

}
