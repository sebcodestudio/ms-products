package com.sebcode.msproducts.product.dto.response.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttributeResponseDTO {
    private Long id;
    private String attributeName;    // "Color"
    private String attributeValue;   // "Rojo"
    private Long attributeTypeId;
    private Long attributeValueId;
    private Boolean isVisual;
    private Integer displayOrder;
}
