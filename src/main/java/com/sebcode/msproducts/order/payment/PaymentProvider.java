package com.sebcode.msproducts.order.payment;

import com.sebcode.msproducts.order.entity.PaymentProviderType;

import java.math.BigDecimal;

/**
 * Abstraccion de pasarela de pago. Order/OrderService solo conocen esta
 * interfaz — agregar otro proveedor (ej. para tarjetas internacionales) es
 * escribir una nueva implementacion, sin tocar el resto del modulo.
 */
public interface PaymentProvider {

    PaymentProviderType getType();

    /**
     * Cobra el monto indicado usando un token ya generado del lado del
     * cliente (la tarjeta nunca llega a nuestro backend).
     */
    ChargeResult charge(BigDecimal amount, String currency, String token, String description, String customerEmail);

}
