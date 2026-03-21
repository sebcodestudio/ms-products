package com.sebcode.msproducts.product.dto.response.customer;

import com.sebcode.msproducts.product.dto.response.shared.ProductImageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantProductDetailPublicResponseDTO {
    private Long id;
    private String sku;

    // Producto padre
    private Long productId;
    private String productName;
    private String productDescription;
    private String brandName;
    private Double productRating;

    // Precios
    private BigDecimal price;
    private BigDecimal originalPrice;
    private BigDecimal finalPrice;
    private Integer discountPercentage;
    private Boolean hasActiveDiscount;
    private LocalDateTime discountEndDate;

    // Stock
    private Integer stock;
    private Boolean available;
    private Integer soldCount;

    // Atributos de esta variante específica
//    private Map<String, String> attributes; // {"Color": "Rojo", "Talla": "M", "Almacenamiento": "128GB"}
//
//    // Todas las imágenes de esta variante
//    private List<ProductImageResponseDTO> images;
//
//    // TODAS las variantes del producto (para cambiar entre variantes)
//    private List<VariantOptionDTO> availableVariants;
//
//    // Atributos disponibles agrupados (para selectores)
//    // {"Color": ["Rojo", "Azul", "Verde"], "Talla": ["S", "M", "L"]}
//    private Map<String, List<AttributeOptionDTO>> availableAttributes;
//
//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class VariantOptionDTO {
//        private Long variantId;
//        private String sku;
//        private Map<String, String> attributes;
//        private BigDecimal price;
//        private BigDecimal finalPrice;
//        private Boolean inStock;
//        private String mainImageUrl;
//    }
//
//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class AttributeOptionDTO {
//        private Long attributeValueId;
//        private String value;
//        private Boolean available; // Si hay stock en alguna variante con este valor
//        private Boolean selected; // Si es el valor de la variante actual
//    }
}