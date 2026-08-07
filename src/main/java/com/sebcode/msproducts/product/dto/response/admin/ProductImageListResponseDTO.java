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
    private Long productId;
    private String productName;
    private String attributeValueName;
    private String imageUrl;
    //    private Integer imageOrder;
    private Boolean isMain;
    //    private String altText;
    private String imageType;
}
