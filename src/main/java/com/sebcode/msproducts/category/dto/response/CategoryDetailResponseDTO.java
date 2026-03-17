package com.sebcode.msproducts.category.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sebcode.msproducts.common.response.AuditableEntityResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "name", "description", "imageUrl"})
public class CategoryDetailResponseDTO extends AuditableEntityResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
}