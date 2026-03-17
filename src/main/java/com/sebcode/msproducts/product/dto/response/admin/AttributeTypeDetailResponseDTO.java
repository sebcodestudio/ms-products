package com.sebcode.msproducts.product.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sebcode.msproducts.common.response.AuditableEntityResponseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "name", "displayOrder", "isVisual"})
public class AttributeTypeDetailResponseDTO extends AuditableEntityResponseDTO {
    private Long id;
    private String name;
    private Integer displayOrder;
    private Boolean isVisual;
}