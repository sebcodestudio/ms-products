package com.sebcode.msproducts.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryListResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String categoryId;

}
