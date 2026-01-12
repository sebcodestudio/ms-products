package com.proyecto.backendtiendavirtual.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_user")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(length = 100, nullable=false, unique=true)
    private String email;

    @NotBlank
    @Size(min = 6, max = 255)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(max = 200)
    @Column(length=100, nullable=false)
    private String name;

    @NotBlank
    @Size(max = 200)
    @Column(name = "last_name", length=100, nullable=false)
    private String lastName;

    @Column(length = 20)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @NotBlank
    @Column(nullable = false)
    private LocalDateTime birthdate;

    @NotBlank
    @Min(0)
    @Max(100)
    @Column(nullable = false, columnDefinition = "INT CHECK (age >= 0 AND age <= 100)")
    private Integer age;

    @NotBlank
    @Column(nullable = false)
    private Boolean gender;

    @Builder.Default
    @Column(nullable=false, columnDefinition="TINYINT(1) DEFAULT TRUE")
    private Boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Cart cart;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CustomerOrder> customerOrders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TokenRecuperation> tokenRecuperations = new ArrayList<>();

    @Column(name="created_user", updatable = false)
    private Long createdUser;

    @CreationTimestamp
    @Builder.Default
    @Column(name="created_at", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="update_user")
    private Long updateUser;

    @UpdateTimestamp
    @Builder.Default
    @Column(name="update_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateAt = LocalDateTime.now();

    // @PrePersist
    // public void prePersist() {
    //     this.state = Boolean.TRUE;
    // }
    
}
