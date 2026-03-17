package com.sebcode.msproducts.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageRequestDTO {

    @NotNull(message = "Variant product ID cannot be null")
    private Long variantProductId;

    @NotNull(message = "Attribute value ID cannot be null")
    private Long attributeValueId;

    @NotBlank(message = "Image URL cannot be blank")
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

    @NotNull(message = "Image order cannot be null")
    @Min(value = 0, message = "Image order must be at least 0")
    private Integer imageOrder;

    @NotNull(message = "Is main flag cannot be null")
    private Boolean isMain;

    @Size(max = 200, message = "Alt text cannot exceed 200 characters")
    private String altText;

    private String imageType; // GALLERY, VARIANT, THUMBNAIL

}
