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
@JsonPropertyOrder({"id", "productId", "productName", "attributeValueId", "attributeValueName", "imageUrl", "imageOrder", "isMain", "altText", "imageType"})
public class ProductImageDetailResponseDTO extends AuditableEntityResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Long attributeValueId;
    private String attributeValueName;
    private String imageUrl;
    private Integer imageOrder;
    private Boolean isMain;
    private String altText;
    private String imageType;
}
