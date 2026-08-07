package com.sebcode.msproducts.complaint.dto.response;

import com.sebcode.msproducts.complaint.entity.ComplaintStatus;
import com.sebcode.msproducts.complaint.entity.ComplaintType;
import com.sebcode.msproducts.complaint.entity.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintListResponseDTO {

    private Long id;
    private ComplaintType type;
    private String consumerName;
    private String consumerLastName;
    private String documentType;
    private String documentNumber;
    private String address;
    private String email;
    private String phone;
    private Boolean isMinor;
    private String guardianName;
    private ProductType productType;
    private BigDecimal amountClaimed;
    private String productDescription;
    private String detail;
    private String consumerRequest;
    private ComplaintStatus status;
    private Boolean notifiedByEmail;
    private LocalDateTime createdAt;

}
