package com.sebcode.msproducts.product.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    @Builder.Default
    private List<VariantProduct> variantProducts = new ArrayList<>();

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "product_subcategory",
//            joinColumns = @JoinColumn(name = "id_product", foreignKey = @ForeignKey(name = "fk_ps_product")),
//            inverseJoinColumns = @JoinColumn(name = "id_subcategory", foreignKey = @ForeignKey(name = "fk_ps_subcategory"))
//    )
//    @Builder.Default
//    private List<Subcategory> subcategories = new ArrayList<>();

    // ============================================
    // LIFECYCLE CALLBACKS
    // ============================================

    @Override
    protected void prePersist() {
        super.prePersist();
        if (this.score == null) this.score = BigDecimal.ZERO;
    }

    // ============================================
    // DOMAIN LOGIC METHODS
    // ============================================

    /**
     * Verifica si el producto está disponible para la venta
     *
     * @return true si está activo, no eliminado y tiene al menos una variante disponible
     */
    public boolean isAvailable() {
        return Boolean.TRUE.equals(this.state) &&
                Boolean.FALSE.equals(this.isDeleted) &&
                hasAvailableVariants();
    }

    /**
     * Verifica si tiene variantes disponibles
     */
    public boolean hasAvailableVariants() {
        return variantProducts != null &&
                variantProducts.stream().anyMatch(VariantProduct::isAvailable);
    }

    /**
     * Obtiene la variante con menor precio
     */
    public BigDecimal getMinPrice() {
        return variantProducts.stream()
                .filter(VariantProduct::isAvailable)
                .map(VariantProduct::getFinalPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Obtiene la variante con mayor precio
     */
    public BigDecimal getMaxPrice() {
        return variantProducts.stream()
                .filter(VariantProduct::isAvailable)
                .map(VariantProduct::getFinalPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Calcula el stock total de todas las variantes
     */
    public int getTotalStock() {
        return variantProducts.stream()
                .filter(v -> Boolean.TRUE.equals(v.getState()) && Boolean.FALSE.equals(v.getIsDeleted()))
                .mapToInt(VariantProduct::getStock)
                .sum();
    }

    /**
     * Calcula el total de unidades vendidas
     */
    public int getTotalSold() {
        return variantProducts.stream()
                .mapToInt(VariantProduct::getSoldCount)
                .sum();
    }

    /**
     * Añade una variante al producto
     */
    public void addVariant(VariantProduct variant) {
        variantProducts.add(variant);
        variant.setProduct(this);
    }

    /**
     * Remueve una variante del producto
     */
    public void removeVariant(VariantProduct variant) {
        variantProducts.remove(variant);
        variant.setProduct(null);
    }

    /**
     * Añade una subcategoría
     */
//    public void addSubcategory(Subcategory subcategory) {
//        if (!subcategories.contains(subcategory)) {
//            subcategories.add(subcategory);
//        }
//    }

    /**
     * Remueve una subcategoría
     */
//    public void removeSubcategory(Subcategory subcategory) {
//        subcategories.remove(subcategory);
//    }

    /**
     * Actualiza el score (rating promedio)
     */
    public void updateScore(BigDecimal newScore) {
        if (newScore == null || newScore.compareTo(BigDecimal.ZERO) < 0 ||
                newScore.compareTo(new BigDecimal("5.0")) > 0) {
            throw new IllegalArgumentException("Score must be between 0.0 and 5.0");
        }
        this.score = newScore;
    }

    // ============================================
    // EQUALS & HASHCODE
    // ============================================

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
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + state +
                '}';
    }
}
