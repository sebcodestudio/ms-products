package com.sebcode.msproducts.company.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import com.sebcode.msproducts.company.enums.ContributorType;
import com.sebcode.msproducts.company.enums.TaxRegime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "companies")
@EntityListeners(AuditingEntityListener.class)
public class Company extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(name = "legal_name", length = 200, nullable = false, unique = true)
    private String legalName;

    @NotBlank
    @Size(max = 200)
    @Column(name = "trade_name", length = 200, nullable = false, unique = true)
    private String tradeName;

    @NotBlank(message = "RUC is required")
    @Pattern(regexp = "^(10|15|17|20)\\d{9}$", message = "Invalid RUC format")
    @Column(length = 11, nullable = false, unique = true)
    private String ruc;

    @Enumerated(EnumType.STRING)
    @Column(name = "contributor_type", length = 20, nullable = false)
    private ContributorType contributorType;

    @NotBlank
    @Size(max = 500)
    @Column(name = "fiscal_address", length = 500, nullable = false)
    private String fiscalAddress;

    @NotNull(message = "Tax regime is required")
    @Column(name = "tax_regime", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaxRegime taxRegime;

//    @Builder.Default
//    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT TRUE")
//    private Boolean active = true;
//
//    @Builder.Default
//    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
//    private Boolean isDeleted = false;
//
//    @Column(name = "delete_date")
//    private LocalDate deleteDate;
//
    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CompanyUser> companyUsers = new ArrayList<>();
//
//    @CreatedBy
//    @Column(name = "created_user", updatable = false)
//    private Long createdUser;
//
//    @CreatedDate
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedBy
//    @Column(name = "update_user")
//    private Long updateUser;
//
//    @LastModifiedDate
//    @Column(name = "update_at")
//    private LocalDateTime updateAt;

    @PrePersist
    @PreUpdate
    private void calculateContributorType() {
        if (ruc != null && ruc.length() >= 2) {
            String prefix = ruc.substring(0, 2);
            this.contributorType = switch (prefix) {
                case "10" -> ContributorType.NATURAL_PERSON;
                case "15" -> ContributorType.NON_RESIDENT;
                case "17" -> ContributorType.STATE_ENTITY;
                case "20" -> ContributorType.LEGAL_ENTITY;
                default -> null;
            };
        }
    }

}
