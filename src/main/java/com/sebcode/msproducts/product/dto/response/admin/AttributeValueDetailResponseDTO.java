package com.sebcode.msproducts.product.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sebcode.msproducts.common.response.AuditableEntityResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "value", "attributeTypeId", "attributeTypeName", "displayOrder"})
public class AttributeValueDetailResponseDTO extends AuditableEntityResponseDTO {
    private Long id;
    private String value;
    private Long attributeTypeId;
    private String attributeTypeName;
    private Integer displayOrder;
}