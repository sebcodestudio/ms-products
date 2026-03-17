package com.sebcode.msproducts.product.dto.response.shared;

import com.sebcode.msproducts.product.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponseDTO {
    private Long id;
    private String imageUrl;
    private Boolean isMain;
    private ImageType imageType; // PRODUCT, VARIANT, LIFESTYLE, DETAIL
    private Integer imageOrder;
}