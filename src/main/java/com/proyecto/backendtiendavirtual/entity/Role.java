package com.proyecto.backendtiendavirtual.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long idRole;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    private String description;

    @Column(name = "created_user", updatable = false)
    private Long createdUser;

    @CreationTimestamp
    @Builder.Default
    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "update_user")
    private Long updateUser;

    @UpdateTimestamp
    @Builder.Default
    @Column(name = "update_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateAt = LocalDateTime.now();
    
}
