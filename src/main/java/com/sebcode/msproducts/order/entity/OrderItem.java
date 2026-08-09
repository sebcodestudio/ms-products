package com.sebcode.msproducts.order.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import com.sebcode.msproducts.product.entity.VariantProduct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
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
@Table(name = "order_items", indexes = {
        @Index(name = "idx_order_items_order", columnList = "id_order"),
        @Index(name = "idx_order_items_variant", columnList = "id_variant_product")
})
public class OrderItem extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order", nullable = false, foreignKey = @ForeignKey(name = "fk_order_item_order"))
    private Order order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variant_product", nullable = false, foreignKey = @ForeignKey(name = "fk_order_item_variant"))
    private VariantProduct variantProduct;

    // Foto del producto/variante al momento de la compra — si el nombre o el
    // precio cambian despues, el pedido historico no debe moverse.
    @NotBlank
    @Size(max = 150)
    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Size(max = 150)
    @Column(name = "variant_label", length = 150)
    private String variantLabel;

    @NotNull
    @Positive
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "line_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;

}
