package com.sebcode.msproducts.product.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sebcode.msproducts.common.response.AuditableEntityResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "variantProductId", "variantProductName", "attributeValueId", "attributeValueName", "imageUrl", "imageOrder", "isMain", "altText", "imageType"})
public class ProductImageDetailResponseDTO extends AuditableEntityResponseDTO {
    private Long id;
    private Long variantProductId;
    private String variantProductSku;
    private Long attributeValueId;
    private String attributeValueName;
    private String imageUrl;
    private Integer imageOrder;
    private Boolean isMain;
    private String altText;
    private String imageType;
}
