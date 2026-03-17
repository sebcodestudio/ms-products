package com.sebcode.msproducts.product.dto.response.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueResponseDTO {
    private Long id;
    private String value;
    private Long attributeTypeId;
    private Integer displayOrder;
}
