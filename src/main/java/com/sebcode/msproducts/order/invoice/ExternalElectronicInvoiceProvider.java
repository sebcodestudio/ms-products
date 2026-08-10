package com.sebcode.msproducts.order.invoice;

import com.fasterxml.jackson.databind.JsonNode;
import com.sebcode.msproducts.order.entity.Order;
import com.sebcode.msproducts.order.entity.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

/**
 * Llama a la API del sistema de facturación electrónica propio (todavía en
 * desarrollo, no en prod al momento de escribir esto) para emitir la
 * boleta/factura del pedido pagado.
 * <p>
 * IMPORTANTE: el shape exacto del request/response de abajo es un supuesto
 * razonable (no el contrato real, que aún no existe) — hay que ajustarlo
 * cuando esa API esté lista y se conozca su formato real. Mientras
 * INVOICING_API_URL no esté configurado, esta clase no intenta ninguna
 * llamada (ver skipped en InvoiceResult) — es seguro tener este código
 * desplegado antes de que el sistema de facturación exista.
 */
@Slf4j
@Component
public class ExternalElectronicInvoiceProvider implements ElectronicInvoiceProvider {

    private final RestClient restClient;
    private final String apiUrl;

    public ExternalElectronicInvoiceProvider(
            @Value("${app.invoicing.api-url:}") String apiUrl,
            @Value("${app.invoicing.api-key:}") String apiKey) {
        this.apiUrl = apiUrl;
        RestClient.Builder builder = RestClient.builder();
        if (StringUtils.hasText(apiKey)) {
            builder.defaultHeader("Authorization", "Bearer " + apiKey);
        }
        this.restClient = builder.build();
    }

    @Override
    public InvoiceResult emitInvoice(Order order) {
        if (!StringUtils.hasText(apiUrl)) {
            log.info("INVOICING_API_URL no configurado — se omite facturación electrónica del pedido {}",
                    order.getPublicReference());
            return InvoiceResult.notConfigured();
        }

        Map<String, Object> body = Map.of(
                "orderReference", order.getPublicReference().toString(),
                "documentType", order.getCustomerDocumentType().name(),
                "documentNumber", order.getCustomerDocumentNumber(),
                "customerName", order.getCustomerName(),
                "customerEmail", order.getCustomerEmail() != null ? order.getCustomerEmail() : "",
                "currency", order.getCurrency(),
                "total", order.getTotal(),
                "items", toItemPayload(order.getItems())
        );

        try {
            JsonNode response = restClient.post()
                    .uri(apiUrl)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);

            String invoiceRef = extractReference(response);
            if (invoiceRef == null) {
                log.error("Facturación electrónica: respuesta 2xx sin referencia de comprobante para pedido {}: {}",
                        order.getPublicReference(), response);
                return InvoiceResult.failure("El sistema de facturación no devolvió un comprobante válido");
            }
            log.info("Boleta/factura {} emitida para pedido {}", invoiceRef, order.getPublicReference());
            return InvoiceResult.success(invoiceRef);
        } catch (RestClientResponseException e) {
            log.error("Facturación electrónica rechazada para pedido {}: {} {}",
                    order.getPublicReference(), e.getStatusCode(), e.getResponseBodyAsString());
            return InvoiceResult.failure("La facturación fue rechazada: " + e.getStatusCode());
        } catch (Exception e) {
            log.error("Fallo de conectividad con el sistema de facturación electrónica para pedido {}",
                    order.getPublicReference(), e);
            return InvoiceResult.failure("No se pudo conectar con el sistema de facturación");
        }
    }

    private List<Map<String, Object>> toItemPayload(List<OrderItem> items) {
        return items.stream()
                .map(item -> Map.<String, Object>of(
                        "description", item.getVariantLabel() != null
                                ? item.getProductName() + " (" + item.getVariantLabel() + ")"
                                : item.getProductName(),
                        "quantity", item.getQuantity(),
                        "unitPrice", item.getUnitPrice(),
                        "lineTotal", item.getLineTotal()
                ))
                .toList();
    }

    private String extractReference(JsonNode response) {
        if (response == null) return null;
        for (String field : List.of("invoiceId", "invoiceReference", "id", "reference")) {
            if (response.has(field)) return response.get(field).asText();
        }
        return null;
    }
}
