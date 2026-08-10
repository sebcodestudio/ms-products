package com.sebcode.msproducts.order.dto.response;

import com.sebcode.msproducts.order.entity.InvoiceStatus;
import com.sebcode.msproducts.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private UUID publicReference;
    private OrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal total;
    private String currency;
    private String paymentFailureReason;
    private InvoiceStatus invoiceStatus;
    private List<OrderItemResponseDTO> items;
    private LocalDateTime createdAt;

}
