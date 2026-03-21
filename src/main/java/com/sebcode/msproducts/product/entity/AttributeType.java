package com.sebcode.msproducts.product.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "attribute_types", indexes = {
        @Index(name = "idx_attr_type_name", columnList = "name")
})
public class AttributeType extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(length = 100, nullable = false, unique = true)
    private String name;

//    OJO: Se puede considerar description en attribute_type
//    @Size(max = 200)
//    @Column(name = "description", length = 200)
//    private String description;

    @NotNull
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder; // Orden de visualización en UI

    @NotNull
    @Column(name = "is_visual", nullable = false)
    private Boolean isVisual; // true si afecta a las imágenes (Color, Sabor)

    @OneToMany(mappedBy = "attributeType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttributeValue> attributeValues;

    @Override
    public void prePersist() {
        super.prePersist();
        if (isVisual == null) {
            isVisual = Boolean.FALSE;
        }
        if (displayOrder == null) {
            displayOrder = 0;
        }
    }

    @PostLoad
    @PrePersist
    private void initCollections() {
        if (attributeValues == null) attributeValues = new ArrayList<>();
    }

}
