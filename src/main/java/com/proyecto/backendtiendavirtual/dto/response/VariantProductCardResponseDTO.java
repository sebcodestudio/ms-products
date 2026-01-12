package com.proyecto.backendtiendavirtual.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

// import com.proyecto.backendtiendavirtual.entity.VariantAttribute;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariantProductCardResponseDTO {

    private Long id;
    private String sku;

    //Datos producto
    private String name;
    private String description;
    private String brand;
    private float score;

    private BigDecimal price;
    private Integer discount;
    private BigDecimal priceDiscount;
    // private List<VariantAttributeDetailResponseDTO> variantAttribute;
    private List<VariantAttributeCardResponseDTO> variantAttributes;

}
