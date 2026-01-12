package com.proyecto.backendtiendavirtual.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "variant_attribute")
public class VariantAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variant")
    private Long idVariantAttribute;

    // @NotBlank
    // @Size(max = 100)
    // @Column(length = 100, nullable = false)
    // private String name;

    // @NotBlank
    // @Size(max = 100)
    // @Column(length = 100, nullable = false)
    // private String value;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "variant_attribute_attribute_value", joinColumns = @JoinColumn(name = "id_variant"), inverseJoinColumns = @JoinColumn(name = "id_attribute_value"))
    @Builder.Default
    private List<AttributeValue> attributeValues = new ArrayList<>();

    @OneToMany(mappedBy = "variantAttribute")
    @Builder.Default
    private List<ProductImg> productImgs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variant_product")
    private VariantProduct variantProduct;

    @Column(name = "created_user", updatable = false)
    private Long createdUser;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;

    @Column(name = "update_user")
    private Long updateUser;

    @UpdateTimestamp
    @Column(name = "update_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

}
