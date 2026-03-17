package com.sebcode.msproducts.product.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import com.sebcode.msproducts.product.enums.ImageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product_images", indexes = {
        @Index(name = "idx_img_variant", columnList = "id_variant_product"),
        @Index(name = "idx_img_main", columnList = "id_variant_product, is_main"),
        @Index(name = "idx_img_visual_attr", columnList = "id_visual_attribute_value")
})
public class ProductImage extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "image_url", nullable = false, unique = true, length = 500)
    private String imageUrl;

    @NotNull
    @Column(name = "image_order", nullable = false)
    private Integer imageOrder;

    @NotNull
    @Column(name = "is_main", nullable = false)
    private Boolean isMain;

    @Size(max = 200)
    @Column(name = "alt_text", length = 200)
    private String altText;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", length = 20)
    private ImageType imageType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variant_product", nullable = false,
            foreignKey = @ForeignKey(name = "fk_img_variant"))
    private VariantProduct variantProduct;

    // OPCIONAL: Vincular imagen con el atributo visual específico
    // Útil para compartir imágenes entre variantes del mismo color
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_visual_attribute_value",
            foreignKey = @ForeignKey(name = "fk_img_visual_attr"))
    private AttributeValue attributeValue; // Ej: "Rojo", "Chocolate"

    @Override
    protected void prePersist() {
        super.prePersist();
        if (imageOrder == null) {
            imageOrder = 0;
        }
        if (isMain == null) {
            isMain = Boolean.FALSE;
        }
        if (imageType == null) {
            imageType = ImageType.GALLERY;
        }
        validateMainImage();
    }

    @Override
    protected void preUpdate() {
        super.preUpdate();
        validateMainImage();
    }

    public void validateMainImage() {
        if (Boolean.TRUE.equals(isMain) && variantProduct != null) {
            // Solo validar si la colección ya está cargada
            if (Hibernate.isInitialized(variantProduct.getProductImages())) {
                variantProduct.getProductImages().stream()
                        .filter(img -> !img.equals(this) && Boolean.TRUE.equals(img.getIsMain()))
                        .forEach(img -> img.setIsMain(false));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductImage)) return false;
        ProductImage that = (ProductImage) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
