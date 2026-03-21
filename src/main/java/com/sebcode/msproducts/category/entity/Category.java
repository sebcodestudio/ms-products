package com.sebcode.msproducts.category.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories", indexes = {
        @Index(name = "idx_imageUrl", columnList = "id_imageUrl"),
        @Index(name = "idx_state_deleted", columnList = "state, is_deleted"),
        @Index(name = "idx_name", columnList = "name")
})
public class Category extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "display_order")
    private Integer displayOrder;

    @OneToMany(mappedBy = "category")
    private List<Subcategory> subcategory;

    @PostLoad
    @PrePersist
    private void initCollections() {
        if (subcategory == null) subcategory = new ArrayList<>();
    }

}
