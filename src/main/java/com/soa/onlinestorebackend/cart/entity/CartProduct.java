package com.soa.onlinestorebackend.cart.entity;

import com.soa.onlinestorebackend.product.entity.VariantProduct;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_products")
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cart", referencedColumnName = "id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variant_product", referencedColumnName = "id", nullable = false)
    private VariantProduct variantProduct;

    @Column(nullable = false)
    private int amount;

}
