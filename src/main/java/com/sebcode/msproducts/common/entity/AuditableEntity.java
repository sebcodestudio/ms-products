package com.sebcode.msproducts.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AuditableEntity {

    // Locking optimista: evita que dos usuarios editando el mismo registro a la
    // vez se pisen en silencio (gana el último PUT sin aviso). Hibernate la
    // maneja sola: null en un registro nuevo, incrementa en cada UPDATE, y
    // lanza ObjectOptimisticLockingFailureException si la versión en el UPDATE
    // ya no coincide con la de la fila (alguien más la modificó primero).
    @Version
    @Column(nullable = false)
    protected Long version;

    @NotNull(message = "State cannot be null")
    @Column(nullable = false)
    protected Boolean state;

    @NotNull(message = "IsDeleted cannot be null")
    @Column(name = "is_deleted", nullable = false)
    protected Boolean isDeleted;

    @Column(name = "delete_at")
    protected LocalDateTime deleteAt;

    @CreatedBy
    @Column(name = "created_user", updatable = false)
    protected Long createdUser;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    protected LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "update_user")
    protected Long updateUser;

    @LastModifiedDate
    @Column(name = "update_at")
    protected LocalDateTime updateAt;

    @PrePersist
    protected void prePersist() {
        if (state == null) state = Boolean.TRUE;
        if (isDeleted == null) isDeleted = Boolean.FALSE;
    }

    @PreUpdate
    protected void preUpdate() {
        if (Boolean.TRUE.equals(isDeleted) && deleteAt == null) {
            deleteAt = LocalDateTime.now();
        }
        if (Boolean.FALSE.equals(isDeleted)) {
            deleteAt = null;
        }
    }

    public void softDelete(Long userId) {
        this.isDeleted = Boolean.TRUE;
        this.deleteAt = LocalDateTime.now();
        this.updateUser = userId;
    }

    public void restore() {
        this.isDeleted = Boolean.FALSE;
        this.deleteAt = null;
    }

    public void activate() {
        this.state = Boolean.TRUE;
    }

    public void deactivate() {
        this.state = Boolean.FALSE;
    }

}
