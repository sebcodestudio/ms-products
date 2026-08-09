package com.sebcode.msproducts.order.entity;

import com.sebcode.msproducts.common.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders", indexes = {
        @Index(name = "idx_orders_public_reference", columnList = "public_reference", unique = true),
        @Index(name = "idx_orders_status", columnList = "status"),
        @Index(name = "idx_orders_state_deleted", columnList = "state, is_deleted")
})
public class Order extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador público usado en URLs/lookups del cliente — nunca el id
    // secuencial, para que no se puedan adivinar/enumerar pedidos ajenos.
    @Builder.Default
    @NotNull
    @Column(name = "public_reference", nullable = false, updatable = false, unique = true)
    private UUID publicReference = UUID.randomUUID();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;

    @NotBlank
    @Size(max = 150)
    @Column(name = "customer_name", nullable = false, length = 150)
    private String customerName;

    @NotBlank
    @Size(max = 30)
    @Column(name = "customer_phone", nullable = false, length = 30)
    private String customerPhone;

    @Size(max = 150)
    @Column(name = "customer_email", length = 150)
    private String customerEmail;

    @NotBlank
    @Size(max = 500)
    @Column(name = "customer_address", nullable = false, length = 500)
    private String customerAddress;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Builder.Default
    @Column(nullable = false, length = 3)
    private String currency = "PEN";

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_provider", length = 20)
    private PaymentProviderType paymentProvider;

    // Id del cargo devuelto por la pasarela (charge id de Culqi, etc).
    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    // Motivo del ultimo intento fallido, si aplica — util para soporte/debug.
    @Size(max = 500)
    @Column(name = "payment_failure_reason", length = 500)
    private String paymentFailureReason;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    private void initOrderDefaults() {
        if (publicReference == null) publicReference = UUID.randomUUID();
        if (status == null) status = OrderStatus.PENDING_PAYMENT;
        if (currency == null) currency = "PEN";
        if (items == null) items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
