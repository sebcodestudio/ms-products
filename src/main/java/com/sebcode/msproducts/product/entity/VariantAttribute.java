package com.sebcode.msproducts.product.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "variant_attributes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_variant_attr_type",
                        columnNames = {"id_variant_product", "id_attribute_type"}
                )
        },
        indexes = {
                @Index(name = "idx_variant_attr_product", columnList = "id_variant_product"),
                @Index(name = "idx_variant_attr_value", columnList = "id_attribute_value")
        }
)
public class VariantAttribute extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variant_product", nullable = false,
            foreignKey = @ForeignKey(name = "fk_var_attr_variant"))
    private VariantProduct variantProduct;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_attribute_type", nullable = false,
            foreignKey = @ForeignKey(name = "fk_var_attr_type"))
    private AttributeType attributeType; // Color, Talla, etc.

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_attribute_value", nullable = false,
            foreignKey = @ForeignKey(name = "fk_var_attr_value"))
    private AttributeValue attributeValue; // Rojo, M, etc.

    // ============================================
    // HELPER METHODS
    // ============================================

    /**
     * Obtiene el nombre del tipo de atributo
     */
    @Transient
    public String getAttributeName() {
        return attributeType != null ? attributeType.getName() : null;
    }

    /**
     * Obtiene el valor del atributo
     */
    @Transient
    public String getValue() {
        return attributeValue != null ? attributeValue.getValue() : null;
    }

    /**
     * Verifica si este atributo afecta a las imágenes
     */
    @Transient
    public boolean isVisualAttribute() {
        return attributeType != null &&
                Boolean.TRUE.equals(attributeType.getIsVisual());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariantAttribute)) return false;
        VariantAttribute that = (VariantAttribute) o;
        return variantProduct != null &&
                attributeType != null &&
                variantProduct.equals(that.variantProduct) &&
                attributeType.equals(that.attributeType);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "VariantAttribute{" +
                "id=" + id +
                ", type=" + getAttributeName() +
                ", value=" + getValue() +
                '}';
    }

}
