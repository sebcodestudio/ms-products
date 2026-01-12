package com.proyecto.backendtiendavirtual.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long idProduct;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT TRUE")
    private Boolean state;

    @Max(value = 5)
    @Column(columnDefinition = "DECIMAL(10,1) CHECK (score <= 5)")
    private float score;
    
    @NotNull
    @Min(0)
    @Column(name = "sold_count", nullable = false, columnDefinition = "INT CHECK (sold_count >= 0)")
    private int soldCount;

    @NotBlank
    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;

    @Column(name = "delete_date")
    private LocalDate deleteDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_brand", nullable = false)
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    @Builder.Default
    private List<VariantProduct> variantProducts = new ArrayList<>();

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_subcategory", joinColumns = @JoinColumn(name = "id_product"), inverseJoinColumns = @JoinColumn(name = "id_subcategory"))
    @Builder.Default
    private List<Subcategory> subcategories = new ArrayList<>();

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
