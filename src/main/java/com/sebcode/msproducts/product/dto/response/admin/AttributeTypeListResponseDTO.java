package com.sebcode.msproducts.product.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeTypeListResponseDTO {
    private Long id;
    private String name;
    private Boolean isVisual;
    private Boolean state;
}