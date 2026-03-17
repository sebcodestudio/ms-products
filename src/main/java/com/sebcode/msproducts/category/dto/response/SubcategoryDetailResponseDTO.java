package com.sebcode.msproducts.category.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sebcode.msproducts.category.entity.Category;
import com.sebcode.msproducts.common.response.AuditableEntityResponseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "name", "description", "imageUrl"})
public class SubcategoryDetailResponseDTO extends AuditableEntityResponseDTO  {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
}
