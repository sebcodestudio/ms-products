package com.sebcode.msproducts.complaint.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "complaints", indexes = {
        @Index(name = "idx_complaints_state_deleted", columnList = "state, is_deleted"),
        @Index(name = "idx_complaints_created_at", columnList = "created_at"),
        @Index(name = "idx_complaints_status", columnList = "status")
})
public class Complaint extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ComplaintType type;

    @NotBlank
    @Size(max = 150)
    @Column(name = "consumer_name", nullable = false, length = 150)
    private String consumerName;

    @NotBlank
    @Size(max = 150)
    @Column(name = "consumer_last_name", nullable = false, length = 150)
    private String consumerLastName;

    @NotBlank
    @Size(max = 20)
    @Column(name = "document_type", nullable = false, length = 20)
    private String documentType;

    @NotBlank
    @Size(max = 30)
    @Column(name = "document_number", nullable = false, length = 30)
    private String documentNumber;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String address;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String email;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String phone;

    @Builder.Default
    @Column(name = "is_minor", nullable = false)
    private Boolean isMinor = Boolean.FALSE;

    @Size(max = 255)
    @Column(name = "guardian_name", length = 255)
    private String guardianName;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 20)
    private ProductType productType;

    @Column(name = "amount_claimed", precision = 10, scale = 2)
    private BigDecimal amountClaimed;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "product_description", nullable = false, length = 1000)
    private String productDescription;

    @NotBlank
    @Size(max = 2000)
    @Column(nullable = false, length = 2000)
    private String detail;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "consumer_request", nullable = false, length = 1000)
    private String consumerRequest;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ComplaintStatus status = ComplaintStatus.PENDIENTE;

    @Builder.Default
    @Column(name = "notified_by_email", nullable = false)
    private Boolean notifiedByEmail = Boolean.FALSE;

    @PrePersist
    private void initComplaintDefaults() {
        if (isMinor == null) isMinor = Boolean.FALSE;
        if (status == null) status = ComplaintStatus.PENDIENTE;
        if (notifiedByEmail == null) notifiedByEmail = Boolean.FALSE;
    }

}
