package com.proyecto.backendtiendavirtual.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "variant_product")
public class VariantProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variant_product")
    private Long idVariantProduct;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false, unique = true)
    private String sku;

    @NotBlank
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2) CHECK (price >= 0)")
    private BigDecimal price;

    private Integer discount;

    @Digits(integer = 10, fraction = 2)
    @Column(name = "price_discount", columnDefinition = "DECIMAL(10,2) CHECK (price_discount >= 0)")
    private BigDecimal priceDiscount;

    @Column(name = "discount_start_date", updatable = false)
    private LocalDateTime discountStartDate;

    @Column(name = "discount_end_date", updatable = false)
    private LocalDateTime discountEndDate;

    @Column(name = "discount_update_user")
    private Long discountUpdateUser;

    @UpdateTimestamp
    @Column(name = "discount_update_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime discountUpdateDate;

    @NotNull
    @Min(0)
    @Column(nullable = false, columnDefinition = "INT CHECK (stock >= 0)")
    private int stock;

    @NotNull
    @Min(0)
    @Column(name = "sold_count", nullable = false, columnDefinition = "INT CHECK (sold_count >= 0)")
    private int soldCount;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT TRUE")
    private Boolean state;

    @NotBlank
    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;

    @Column(name = "delete_date")
    private LocalDate deleteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "variantProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "variantProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VariantAttribute> variantAttributes = new ArrayList<>();

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

    @PrePersist
    public void prePersist() {
        this.state = Boolean.TRUE;
        this.isDeleted = Boolean.FALSE;
    }

}
