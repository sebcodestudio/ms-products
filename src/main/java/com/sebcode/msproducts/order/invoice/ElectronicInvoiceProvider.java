package com.sebcode.msproducts.order.invoice;

import com.sebcode.msproducts.order.entity.Order;

/**
 * Envía el pedido pagado al sistema de facturación electrónica (SUNAT vía un
 * PSE/sistema propio) para que emita la boleta/factura y notifique al
 * cliente. Es "best-effort" por diseño: OrderServiceImpl nunca debe fallar
 * ni revertir un pago exitoso porque la facturación falle o no esté
 * disponible — ver ExternalElectronicInvoiceProvider.
 */
public interface ElectronicInvoiceProvider {

    InvoiceResult emitInvoice(Order order);

}
