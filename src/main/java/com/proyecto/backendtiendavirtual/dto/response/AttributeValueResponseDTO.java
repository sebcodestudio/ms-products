package com.proyecto.backendtiendavirtual.dto.response;

// import java.util.List;

// import com.proyecto.backendtiendavirtual.entity.VariantAttribute;

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
    private String type;
    private String value;
    
    // private List<VariantAttributeCardResponseDTO> variantAttributes;
    // private AttributeTypeCardResponseDTO attributeType;

}
