package com.sebcode.msproducts.product.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "attribute_values",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_attr_type_value",
                        columnNames = {"id_attribute_type", "value"}
                )
        },
        indexes = {
                @Index(name = "idx_attr_value_type", columnList = "id_attribute_type"),
                @Index(name = "idx_attr_value_state", columnList = "state")
        }
)
public class AttributeValue extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Value cannot be blank")
    @Size(max = 100, message = "Value cannot exceed 100 characters")
    @Column(length = 100, nullable = false)
    private String value; // "Rojo", "Azul", "S", "M", "L", "128GB"

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_attribute_type", nullable = false,
            foreignKey = @ForeignKey(name = "fk_attr_value_type"))
    private AttributeType attributeType;

//    OJO: Anailizar si es necesario hexcolor para que es
//    @Column(name = "hex_color", length = 7)
//    private String hexColor; // Para colores: "#FF0000"

    @Column(name = "display_order")
    private Integer displayOrder; // Orden de visualización en UI

    @Override
    protected void prePersist() {
        super.prePersist();
        if (displayOrder == null) {
            displayOrder = 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttributeValue)) return false;
        AttributeValue that = (AttributeValue) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
