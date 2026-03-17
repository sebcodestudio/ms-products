package com.sebcode.msproducts.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AuditableEntityResponseDTO {
    private Boolean state;
    private Boolean isDeleted;
    private LocalDateTime deleteAt;
    private Long createdUser;
    private LocalDateTime createdAt;
    private Long updateUser;
    private LocalDateTime updateAt;
}
