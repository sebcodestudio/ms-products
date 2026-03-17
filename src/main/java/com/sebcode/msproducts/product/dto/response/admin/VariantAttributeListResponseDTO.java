package com.sebcode.msproducts.product.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttributeListResponseDTO {
    private Long id;
    //    private Long variantProductId;
    private String variantProductName;
    //    private Long attributeTypeId;
    private String attributeTypeName;
    //    private Long attributeValueId;
    private String attributeValue;
}
