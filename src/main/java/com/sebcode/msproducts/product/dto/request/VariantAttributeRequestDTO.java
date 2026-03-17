package com.sebcode.msproducts.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttributeRequestDTO {

    @NotNull(message = "Variant product ID cannot be null")
    private Long variantProductId;

    @NotNull(message = "Attribute type ID cannot be null")
    private Long attributeTypeId;

    @NotNull(message = "Attribute value ID cannot be null")
    private Long attributeValueId;

}