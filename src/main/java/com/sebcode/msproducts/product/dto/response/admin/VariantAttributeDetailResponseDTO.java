package com.sebcode.msproducts.product.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sebcode.msproducts.common.response.AuditableEntityResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "variantProductId", "variantProductName", "attributeTypeId", "attributeTypeName", "attributeValueId", "attributeValue"})
public class VariantAttributeDetailResponseDTO extends AuditableEntityResponseDTO {
    private Long id;
    private Long variantProductId;
    private String variantProductName;
    private Long attributeTypeId;
    private String attributeTypeName;
    private Long attributeValueId;
    private String attributeValue;
}
