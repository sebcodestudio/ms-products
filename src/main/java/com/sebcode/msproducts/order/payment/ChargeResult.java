package com.sebcode.msproducts.order.payment;

/**
 * Resultado de un intento de cobro contra una pasarela de pago, ya
 * normalizado — el resto del sistema no necesita saber nada especifico de
 * Culqi (ni de cualquier otro proveedor que se agregue despues).
 */
public record ChargeResult(boolean success, String paymentReference, String failureReason) {

    public static ChargeResult success(String paymentReference) {
        return new ChargeResult(true, paymentReference, null);
    }

    public static ChargeResult failure(String reason) {
        return new ChargeResult(false, null, reason);
    }
}
