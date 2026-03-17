package com.sebcode.msproducts.product.dto.response.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantProductListPublicResponseDTO {
    private Long id;
    private String sku;

    // Info del producto
    private Long productId;
    private String productName;
    private String brandName;

    // Precios
    private BigDecimal price;
    private BigDecimal finalPrice;
    private Integer discountPercentage;
    private Boolean hasDiscount;

    // Stock
    private Boolean inStock;
    private Boolean lowStock; // stock < 10

    // Atributos principales (ej: "Rojo" si es Color)
    private Map<String, String> mainAttributes; // {"Color": "Rojo", "Talla": "M"}

    // Imagen principal
    private String mainImageUrl;

    // Info adicional
    private Integer soldCount;
    private Boolean isNew; // createdAt < 30 días
    private Double productRating; // del producto padre
}
