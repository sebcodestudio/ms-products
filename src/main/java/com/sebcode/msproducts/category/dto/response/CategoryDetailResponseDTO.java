package com.sebcode.msproducts.category.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sebcode.msproducts.common.response.AuditableEntityResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "description", "imageUrl"})
public class CategoryDetailResponseDTO extends AuditableEntityResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
}