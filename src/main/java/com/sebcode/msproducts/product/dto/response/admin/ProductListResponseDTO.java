package com.sebcode.msproducts.product.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponseDTO {
    private Long id;
    private String name;
    private float score;
    private String brand;
    private List<String> subcategories;
}