package com.sebcode.msproducts.product.dto.response.admin;

import com.sebcode.msproducts.common.response.AuditableEntityResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDTO extends AuditableEntityResponseDTO {
    private Long id;
    private String name;
    private String description;
    private float score;
    private Long brandId;
    private String brand;
}
