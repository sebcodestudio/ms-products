package com.proyecto.backendtiendavirtual.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subcategory")
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subcategory")
    private Long idSubcategory;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotBlank
    @Size(max = 255)
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT TRUE")
    private Boolean state;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    private Category category;

    @ManyToMany(mappedBy = "subcategories")
    private List<Product> products;

}
