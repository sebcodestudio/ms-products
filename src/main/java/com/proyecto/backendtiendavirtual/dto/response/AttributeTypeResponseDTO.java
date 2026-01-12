package com.proyecto.backendtiendavirtual.dto.response;

// import java.util.List;

// import com.proyecto.backendtiendavirtual.entity.AttributeValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeTypeResponseDTO {

    private Long id;
    private String name;
    // private List<AttributeValueCardResponseDTO> attributeValues;

}
