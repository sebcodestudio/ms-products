package com.proyecto.backendtiendavirtual.dto.response;

import java.util.List;

// import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantAttributeDetailResponseDTO {

    private Long id;
    // private String name;
    // private String value;
    // private List<ProductImgResponseDTO> productImgs;
    private List<AttributeValueResponseDTO> attributeValues;
    private List<ProductImgResponseDTO> productImgs;

}
