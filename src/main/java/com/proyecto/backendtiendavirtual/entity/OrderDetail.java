package com.proyecto.backendtiendavirtual.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_detail")
    private Long idOrderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer_order")
    private CustomerOrder customerOrder;

    @Column(nullable = false)
    private int amount;

    @Column(name = "price_unit", scale = 2, nullable = false)
    private BigDecimal priceUnit;
    
}
