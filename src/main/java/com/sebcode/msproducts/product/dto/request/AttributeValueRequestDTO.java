package com.sebcode.msproducts.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueRequestDTO {

    @NotNull(message = "Attribute type ID cannot be null")
    private Long attributeTypeId;

    @NotBlank(message = "Value cannot be blank")
    @Size(max = 100, message = "Value cannot exceed 100 characters")
    private String value;

    @Min(value = 0, message = "Display order must be at least 0")
    private Integer displayOrder;

}
