package com.sebcode.msproducts.product.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sebcode.msproducts.common.response.AuditableEntityResponseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "name", "description", "score", "brandId", "brand", "subcategories"})
public class ProductDetailResponseDTO extends AuditableEntityResponseDTO {
    private Long id;
    private String name;
    private String description;
    private float score;
    private Long brandId;
    private String brand;
    private List<SubcategoryRefDTO> subcategories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubcategoryRefDTO {
        private Long id;
        private String name;
    }
}