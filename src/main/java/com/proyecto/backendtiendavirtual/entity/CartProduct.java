package com.proyecto.backendtiendavirtual.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_product")
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart_product")
    private Long idCartProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cart", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variant_product", nullable = false)
    private VariantProduct variantProduct;

    @Column(nullable = false)
    private int amount;
    
}
