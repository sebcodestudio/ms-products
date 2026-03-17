package com.sebcode.msproducts.product.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueListResponseDTO {
    private Long id;
    private String value;
//    private Long attributeTypeId;
    private String attributeTypeName;
    private Boolean state;
}