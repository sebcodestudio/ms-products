package com.sebcode.msproducts.product.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageListResponseDTO {
    private Long id;
    //    private Long variantProductId;
    private Long variantProductSku;
    private String attributeValueName;
    private String imageUrl;
    //    private Integer imageOrder;
    private Boolean isMain;
    //    private String altText;
    private String imageType;
}
