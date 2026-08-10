package com.sebcode.msproducts.order.invoice;

import com.sebcode.msproducts.order.entity.DocumentType;
import com.sebcode.msproducts.order.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Simula la emisión de boleta/factura sin llamar a ningún sistema externo —
 * permite validar que el pedido dispara la facturación apenas queda PAID
 * (invoiceStatus, invoiceReference) mientras el sistema de facturación real
 * todavía no tiene contrato definido. Se activa con app.invoicing.mode=mock;
 * es el default en application-dev.yml.
 * <p>
 * Convención para probar el camino de fallo: si el número de documento
 * termina en "000" se simula un rechazo de facturación.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.invoicing", name = "mode", havingValue = "mock")
public class MockElectronicInvoiceProvider implements ElectronicInvoiceProvider {

    @Override
    public InvoiceResult emitInvoice(Order order) {
        if (order.getCustomerDocumentNumber() != null && order.getCustomerDocumentNumber().endsWith("000")) {
            log.warn("[FACTURACIÓN SIMULADA] rechazo simulado para pedido {}", order.getPublicReference());
            return InvoiceResult.failure("Documento rechazado (simulado — número de prueba termina en 000)");
        }

        String prefix = order.getCustomerDocumentType() == DocumentType.RUC ? "F001-" : "B001-";
        String invoiceRef = prefix + String.format("%08d", Math.abs(order.getPublicReference().hashCode() % 100000000));

        log.warn("[FACTURACIÓN SIMULADA] boleta/factura {} emitida para pedido {}", invoiceRef, order.getPublicReference());
        return InvoiceResult.success(invoiceRef);
    }
}
