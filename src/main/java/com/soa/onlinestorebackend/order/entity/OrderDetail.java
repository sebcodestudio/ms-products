package com.soa.onlinestorebackend.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer_order", referencedColumnName = "id")
    private CustomerOrder customerOrder;

    @Column(nullable = false)
    private int amount;

    @Column(name = "price_unit", scale = 2, nullable = false)
    private BigDecimal priceUnit;

}
