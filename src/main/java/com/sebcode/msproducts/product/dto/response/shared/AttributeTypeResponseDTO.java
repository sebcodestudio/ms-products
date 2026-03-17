package com.sebcode.msproducts.product.dto.response.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeTypeResponseDTO {
    private Long id;
    private String name;
    private Integer displayOrder;
    private Boolean isVisual;
}
