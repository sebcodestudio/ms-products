package com.sebcode.msproducts.complaint.dto.response;

import com.sebcode.msproducts.complaint.entity.ComplaintStatus;
import com.sebcode.msproducts.complaint.entity.ComplaintType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponseDTO {

    private Long id;
    private ComplaintType type;
    private ComplaintStatus status;
    private LocalDateTime createdAt;

}
