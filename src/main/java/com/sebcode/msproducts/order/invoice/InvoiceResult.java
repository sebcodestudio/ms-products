package com.sebcode.msproducts.order.invoice;

/**
 * Resultado normalizado de intentar emitir la boleta/factura electronica.
 * "skipped" distingue "no configurado todavia" (no es un error, se omite a
 * proposito) de un fallo real de la llamada.
 */
public record InvoiceResult(boolean success, boolean skipped, String invoiceReference, String failureReason) {

    public static InvoiceResult success(String invoiceReference) {
        return new InvoiceResult(true, false, invoiceReference, null);
    }

    public static InvoiceResult failure(String reason) {
        return new InvoiceResult(false, false, null, reason);
    }

    public static InvoiceResult notConfigured() {
        return new InvoiceResult(false, true, null, "Sistema de facturación electrónica no configurado");
    }
}
