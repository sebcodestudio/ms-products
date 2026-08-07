package com.sebcode.msproducts.company.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import com.sebcode.msproducts.company.enums.CompanyRole;
import com.sebcode.msproducts.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company_user")
@EntityListeners(AuditingEntityListener.class)
public class CompanyUser extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyRole role;

//    @Builder.Default
//    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT TRUE")
//    private Boolean active = true;
//
//    @Builder.Default
//    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
//    private Boolean isDeleted = false;
//
//    @Column(name = "created_user", updatable = false)
//    private Long createdUser;
//
//    @CreationTimestamp
//    @Builder.Default
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Column(name = "update_user")
//    private Long updateUser;
//
//    @UpdateTimestamp
//    @Builder.Default
//    @Column(name = "update_at")
//    private LocalDateTime updateAt = LocalDateTime.now();

}
