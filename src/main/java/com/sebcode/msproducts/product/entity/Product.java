package com.sebcode.msproducts.product.entity;

import com.sebcode.msproducts.category.entity.Subcategory;
import com.sebcode.msproducts.common.entity.AuditableEntity;
import com.sebcode.msproducts.product.enums.ImageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products", indexes = {
        @Index(name = "idx_brand", columnList = "id_brand"),
        @Index(name = "idx_state_deleted", columnList = "state, is_deleted"),
        @Index(name = "idx_name", columnList = "name")
})
public class Product extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(length = 100, nullable = false)
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @DecimalMin(value = "0.0", message = "Score must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Score cannot exceed 5.0")
    @Digits(integer = 1, fraction = 1, message = "Score must have max 1 integer digit and 1 decimal place")
    @Column(precision = 2, scale = 1)
    private BigDecimal score;

    @NotNull(message = "Brand cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_brand", nullable = false, foreignKey = @ForeignKey(name = "fk_product_brand"))
    private Brand brand;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_subcategory",
            joinColumns = @JoinColumn(name = "id_product", foreignKey = @ForeignKey(name = "fk_ps_product")),
            inverseJoinColumns = @JoinColumn(name = "id_subcategory", foreignKey = @ForeignKey(name = "fk_ps_subcategory"))
    )
    @Builder.Default
    private List<Subcategory> subcategories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    @Builder.Default
    private List<VariantProduct> variantProducts = new ArrayList<>();

    // Imágenes del producto. Sin attributeValue = genérica (usada en catálogo);
    // con attributeValue (ej. Color=Rojo) = solo aplica a variantes con ese atributo.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("imageOrder ASC")
    @BatchSize(size = 10)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    // ============================================
    // LIFECYCLE CALLBACKS
    // ============================================

    @Override
    protected void prePersist() {
        super.prePersist();
        if (this.score == null) this.score = BigDecimal.ZERO;
        if (this.variantProducts == null) this.variantProducts = new ArrayList<>();
        if (this.subcategories == null) this.subcategories = new ArrayList<>();
        if (this.images == null) this.images = new ArrayList<>();
    }

    // ============================================
    // DOMAIN LOGIC METHODS
    // ============================================

    public boolean isAvailable() {
        return Boolean.TRUE.equals(this.state) &&
                Boolean.FALSE.equals(this.isDeleted) &&
                hasAvailableVariants();
    }

    public boolean hasAvailableVariants() {
        return variantProducts != null &&
                variantProducts.stream().anyMatch(VariantProduct::isAvailable);
    }

    public BigDecimal getMinPrice() {
        return variantProducts.stream()
                .filter(VariantProduct::isAvailable)
                .map(VariantProduct::getFinalPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getMaxPrice() {
        return variantProducts.stream()
                .filter(VariantProduct::isAvailable)
                .map(VariantProduct::getFinalPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public int getTotalStock() {
        return variantProducts.stream()
                .filter(v -> Boolean.TRUE.equals(v.getState()) && Boolean.FALSE.equals(v.getIsDeleted()))
                .mapToInt(VariantProduct::getStock)
                .sum();
    }

    public int getTotalSold() {
        return variantProducts.stream()
                .mapToInt(VariantProduct::getSoldCount)
                .sum();
    }

    public void addVariant(VariantProduct variant) {
        variantProducts.add(variant);
        variant.setProduct(this);
    }

    public void addImage(String url, boolean isMain, ImageType type, AttributeValue attributeValue) {
        ProductImage img = ProductImage.builder()
                .product(this)
                .imageUrl(url)
                .isMain(isMain)
                .imageType(type)
                .attributeValue(attributeValue)
                .imageOrder(images.size())
                .build();
        images.add(img);
    }

    /**
     * Imagen "general" del producto, usada en catálogo/listados donde no se
     * quiere que la foto cambie según el color de la variante mostrada.
     */
    public String getMainImageUrl() {
        if (images == null || images.isEmpty()) return null;
        return images.stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsMain()))
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(images.stream()
                        .min(Comparator.comparingInt(img -> img.getImageOrder() != null ? img.getImageOrder() : 0))
                        .map(ProductImage::getImageUrl)
                        .orElse(null));
    }

    public void addSubcategory(Subcategory subcategory) {
        if (!subcategories.contains(subcategory)) {
            subcategories.add(subcategory);
        }
    }

    public void removeSubcategory(Subcategory subcategory) {
        subcategories.remove(subcategory);
    }

    public void updateScore(BigDecimal newScore) {
        if (newScore == null || newScore.compareTo(BigDecimal.ZERO) < 0 ||
                newScore.compareTo(new BigDecimal("5.0")) > 0) {
            throw new IllegalArgumentException("Score must be between 0.0 and 5.0");
        }
        this.score = newScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', state=" + state + '}';
    }
}