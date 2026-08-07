package com.sebcode.msproducts.product.entity;

//import com.soa.onlinestorebackend.cart.entity.CartProduct;
import com.sebcode.msproducts.common.entity.AuditableEntity;
import com.sebcode.msproducts.product.enums.ImageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "variant_products", indexes = {
        // Removido idx_sku porque ya es unique y MySQL crea índice automático
        @Index(name = "idx_product_id", columnList = "id_product"),
//        @Index(name = "idx_state_deleted", columnList = "state, is_deleted"),
        @Index(name = "idx_discount_dates", columnList = "discount_start_date, discount_end_date"),
        // Nuevo: para búsquedas por stock disponible
        @Index(name = "idx_stock", columnList = "stock"),
        @Index(name = "idx_available", columnList = "state, is_deleted, stock")
})
public class VariantProduct extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version")
// prueba           , nullable = false)
    private Long version; // JPA maneja automáticamente

    @NotBlank(message = "SKU cannot be blank")
    @Size(max = 100, message = "SKU cannot exceed 100 characters")
    @Column(length = 100, nullable = false, unique = true)
    private String sku;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Price must be at least 0.00")
    @Digits(integer = 10, fraction = 2, message = "Price must have max 10 integer digits and 2 decimal places")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Min(value = 0, message = "Discount must be at least 0")
    @Max(value = 100, message = "Discount cannot exceed 100")
    @Column(name = "discount")
    private Integer discount;

    @DecimalMin(value = "0.00", inclusive = true, message = "Original price must be at least 0.00")
    @Digits(integer = 10, fraction = 2, message = "Original price must have max 10 integer digits and 2 decimal places")
    @Column(name = "original_price", precision = 12, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "discount_start_date")
    private LocalDateTime discountStartDate;

    @Column(name = "discount_end_date")
    private LocalDateTime discountEndDate;

    @Column(name = "discount_update_user")
    private Long discountUpdateUser;

    @Column(name = "discount_update_date")
    private LocalDateTime discountUpdateDate;

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0, message = "Stock must be at least 0")
    @Column(nullable = false)
    private Integer stock;

    @NotNull(message = "Sold count cannot be null")
    @Min(value = 0, message = "Sold count must be at least 0")
    @Column(name = "sold_count", nullable = false)
    private Integer soldCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false, foreignKey = @ForeignKey(name = "fk_variant_product"))
    private Product product;

//    @OneToMany(mappedBy = "variantProduct", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "variantProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VariantAttribute> variantAttributes = new ArrayList<>();

    // ============================================
    // LIFECYCLE CALLBACKS
    // ============================================

    @Override
    public void prePersist() {
        super.prePersist();
        if (this.originalPrice == null && this.price != null) {
            this.originalPrice = this.price;
        }
        if (this.stock == null) {
            this.stock = 0;
        }
        if (this.soldCount == null) {
            this.soldCount = 0;
        }
        if (this.discount == null) {
            this.discount = 0;
        }
        validateSku();
        validateDiscountDates();
        validateBusinessRules();
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        validateSku();
        validateDiscountDates();
        validateBusinessRules();
    }

    // ============================================
    // DOMAIN LOGIC METHODS
    // ============================================

    /**
     * Calcula el precio final aplicando el descuento si está activo
     *
     * @return precio final con descuento aplicado
     */
    public BigDecimal getFinalPrice() {
        if (isDiscountActive()) {
            BigDecimal discountAmount = price.multiply(BigDecimal.valueOf(discount))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            return price.subtract(discountAmount);
        }
        return price;
    }

    /**
     * Verifica si el descuento está activo considerando porcentaje y fechas
     *
     * @return true si el descuento es aplicable en este momento
     */
    public boolean isDiscountActive() {
        if (discount == null || discount <= 0) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        boolean afterStart = discountStartDate == null || !now.isBefore(discountStartDate);
        boolean beforeEnd = discountEndDate == null || !now.isAfter(discountEndDate);

        return afterStart && beforeEnd;
    }

    /**
     * Verifica si el producto está disponible para la venta
     *
     * @return true si está activo, no eliminado y tiene stock
     */
    public boolean isAvailable() {
        return Boolean.TRUE.equals(state) &&
                Boolean.FALSE.equals(isDeleted) &&
                stock != null &&
                stock > 0;
    }

    /**
     * Reduce el stock y aumenta el contador de ventas
     *
     * @param quantity cantidad a reducir (debe ser positiva)
     * @throws IllegalArgumentException si la cantidad no es positiva
     * @throws IllegalStateException    si no hay stock suficiente
     */
    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (this.stock == null || this.stock < quantity) {
            throw new IllegalStateException(
                    String.format("Insufficient stock. Requested: %d, Available: %d",
                            quantity, this.stock == null ? 0 : this.stock)
            );
        }
        // Disparar evento si stock está bajo
        if (this.stock <= 5) {
            // publishEvent(new LowStockEvent(this));
        }
        this.stock -= quantity;
        this.soldCount = (this.soldCount == null ? 0 : this.soldCount) + quantity;
    }

    /**
     * Aumenta el stock (usado en reabastecimiento o devoluciones)
     *
     * @param quantity cantidad a añadir (debe ser positiva)
     * @param isReturn true si es una devolución (decrementará soldCount)
     * @throws IllegalArgumentException si la cantidad no es positiva
     */
    public void increaseStock(int quantity, boolean isReturn) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.stock = (this.stock == null ? 0 : this.stock) + quantity;

        // Si es una devolución, decrementar el contador de vendidos
        if (isReturn && this.soldCount != null && this.soldCount >= quantity) {
            this.soldCount -= quantity;
        }
    }

    /**
     * Aumenta el stock (reabastecimiento normal)
     *
     * @param quantity cantidad a añadir
     */
    public void increaseStock(int quantity) {
        increaseStock(quantity, false);
    }

    /**
     * Establece un descuento con fechas de vigencia
     *
     * @param discountPercentage porcentaje de descuento (0-100)
     * @param startDate          fecha de inicio (puede ser null)
     * @param endDate            fecha de fin (puede ser null)
     * @param userId             usuario que aplica el descuento
     * @throws IllegalArgumentException si el descuento es inválido o las fechas son incorrectas
     */
    public void applyDiscount(Integer discountPercentage, LocalDateTime startDate,
                              LocalDateTime endDate, Long userId) {
        if (discountPercentage == null || discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        // Solo guardar original si es la primera vez O si no había descuento activo
        if (this.originalPrice == null) {
            this.originalPrice = this.price;
        }

        this.discount = discountPercentage;
        this.discountStartDate = startDate;
        this.discountEndDate = endDate;
        this.discountUpdateUser = userId;
        this.discountUpdateDate = LocalDateTime.now();
    }

    /**
     * Elimina el descuento activo
     */
    public void removeDiscount() {
        this.discount = 0;
        this.discountStartDate = null;
        this.discountEndDate = null;
    }

    /**
     * Marca el producto como eliminado (soft delete)
     *
     * @param userId usuario que realiza la eliminación
//     */
//    public void softDelete(Long userId) {
//        this.isDeleted = Boolean.TRUE;
//        this.deleteDate = LocalDateTime.now();
//        this.updateUser = userId;
//    }
//
//    /**
//     * Restaura un producto eliminado
//     */
//    public void restore() {
//        this.isDeleted = Boolean.FALSE;
//        this.deleteDate = null;
//    }

    /**
     * Activa el producto
     */
    public void activate() {
        this.state = Boolean.TRUE;
    }

    /**
     * Desactiva el producto
     */
    public void deactivate() {
        this.state = Boolean.FALSE;
    }

    // Separar normalización de validación
    private void normalizeSku() {
        if (sku != null) {
            sku = sku.trim().toUpperCase();
        }
    }

    private void validateSku() {
        if (sku == null || sku.isBlank()) {
            throw new IllegalStateException("SKU cannot be blank");
        }
        // Validaciones adicionales (formato, etc.)
    }

    private void validateBusinessRules() {
        if (discount != null && discount > 0) {
            if (originalPrice == null || originalPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException("Original price must be set when discount is applied");
            }
        }
    }

    // ============================================
    // PRIVATE HELPER METHODS
    // ============================================

    /**
     * Valida que las fechas de descuento sean coherentes
     *
     * @throws IllegalStateException si las fechas son inválidas
     */
    private void validateDiscountDates() {
        if (discountStartDate != null && discountEndDate != null) {
            if (discountEndDate.isBefore(discountStartDate)) {
                throw new IllegalStateException(
                        "Discount end date cannot be before start date"
                );
            }
        }
    }

    /**
     * Obtiene el valor de un atributo específico
     */
    @Transient
    public String getAttributeValue(String attributeName) {
        return variantAttributes.stream()
                .filter(va -> attributeName.equals(va.getAttributeName()))
                .map(VariantAttribute::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene todos los atributos como Map
     */
    @Transient
    public Map<String, String> getAttributesMap() {
        return variantAttributes.stream()
                .collect(Collectors.toMap(
                        VariantAttribute::getAttributeName,
                        VariantAttribute::getValue,
                        (v1, v2) -> v1
                ));
    }

    /**
     * Genera nombre completo con atributos
     */
    @Transient
    public String getDisplayName() {
        if (product == null) return sku;

        String attributes = variantAttributes.stream()
                .sorted(Comparator.comparing(va -> va.getAttributeType().getDisplayOrder()))
                .map(VariantAttribute::getValue)
                .collect(Collectors.joining(", "));

        return product.getName() +
                (attributes.isEmpty() ? "" : " - " + attributes);
    }

    /**
     * Imágenes de esta variante, resueltas desde las imágenes del producto padre.
     * Prioriza las imágenes ligadas a los valores de atributo visual de esta variante
     * (ej. Color=Rojo comparte foto entre todas las tallas de Rojo). Si la variante no
     * tiene atributos visuales o no hay imágenes para ese valor, cae a las imágenes
     * genéricas del producto (sin attributeValue).
     */
    @Transient
    public List<ProductImage> getResolvedImages() {
        if (product == null || product.getImages() == null || product.getImages().isEmpty()) {
            return Collections.emptyList();
        }

        Comparator<ProductImage> byOrder = Comparator.comparingInt(
                img -> img.getImageOrder() != null ? img.getImageOrder() : 0);

        Set<Long> myVisualValueIds = variantAttributes.stream()
                .filter(VariantAttribute::isVisualAttribute)
                .map(va -> va.getAttributeValue() != null ? va.getAttributeValue().getId() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!myVisualValueIds.isEmpty()) {
            List<ProductImage> matched = product.getImages().stream()
                    .filter(img -> img.getAttributeValue() != null
                            && myVisualValueIds.contains(img.getAttributeValue().getId()))
                    .sorted(byOrder)
                    .collect(Collectors.toList());
            if (!matched.isEmpty()) return matched;
        }

        List<ProductImage> generic = product.getImages().stream()
                .filter(img -> img.getAttributeValue() == null)
                .sorted(byOrder)
                .collect(Collectors.toList());
        if (!generic.isEmpty()) return generic;

        return product.getImages().stream().sorted(byOrder).collect(Collectors.toList());
    }

    @Transient
    public String getResolvedMainImageUrl() {
        List<ProductImage> resolved = getResolvedImages();
        if (resolved.isEmpty()) return null;
        return resolved.stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsMain()))
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(resolved.get(0).getImageUrl());
    }
    @AssertTrue(message = "Discount end date must be after start date")
    private boolean isValidDiscountPeriod() {
        if (discountStartDate == null || discountEndDate == null) {
            return true;
        }
        return !discountEndDate.isBefore(discountStartDate);
    }

    /**
     * Añade un atributo
     */
    public void addAttribute(AttributeType type, AttributeValue value) {
        VariantAttribute attr = VariantAttribute.builder()
                .variantProduct(this)
                .attributeType(type)
                .attributeValue(value)
                .build();
        variantAttributes.add(attr);
    }

    // ============================================
    // EQUALS & HASHCODE (importante para JPA)
    // ============================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariantProduct)) return false;
        VariantProduct that = (VariantProduct) o;
        return sku != null && sku.equals(that.sku);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // Agregar este método en VariantProduct para consistencia
    public boolean hasProduct() {
        return this.product != null;
    }

    // Mejorar toString para evitar lazy loading issues
    @Override
    public String toString() {
        return "VariantProduct{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", finalPrice=" + getFinalPrice() +
                ", stock=" + stock +
                ", available=" + isAvailable() +
                '}';
    }
}