package com.proyecto.backendtiendavirtual.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(name = "attribute_type")
public class AttributeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_attribute_type")
    private Long idAttributeType;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "attributeType")
    @Builder.Default
    private List<AttributeValue> attributeValues = new ArrayList<>();

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
