package com.sebcode.msproducts.order.invoice;

import com.fasterxml.jackson.databind.JsonNode;
import com.sebcode.msproducts.order.entity.DocumentType;
import com.sebcode.msproducts.order.entity.Order;
import com.sebcode.msproducts.order.entity.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Llama a la API real del sistema de facturación electrónica propio
 * ("facturacion-app", Quarkus) para emitir la boleta/factura del pedido
 * pagado. Contrato tomado directamente de su OpenAPI (POST
 * /api/comprobantes/boletas | /facturas) — ya no es un supuesto.
 * <p>
 * Activo salvo que app.invoicing.mode=mock (ver MockElectronicInvoiceProvider)
 * — en local (application-dev.yml) el mock es el default hasta que
 * INVOICING_API_URL/INVOICING_API_KEY/datos del emisor estén configurados.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.invoicing", name = "mode", havingValue = "external", matchIfMissing = true)
public class ExternalElectronicInvoiceProvider implements ElectronicInvoiceProvider {

    // Los precios del catálogo incluyen IGV (18%); SUNAT pide valor de venta
    // (sin IGV) y el monto de IGV por separado en cada ítem.
    private static final BigDecimal IGV_RATE = new BigDecimal("0.18");
    private static final BigDecimal ONE_PLUS_IGV = BigDecimal.ONE.add(IGV_RATE);

    private final RestClient restClient;
    private final String apiUrl;
    private final String serieBoleta;
    private final String serieFactura;
    private final String emisorRuc;
    private final String emisorRazonSocial;
    private final String emisorNombreComercial;
    private final String emisorUbigeo;
    private final String emisorDireccion;

    public ExternalElectronicInvoiceProvider(
            @Value("${app.invoicing.api-url:}") String apiUrl,
            @Value("${app.invoicing.api-key:}") String apiKey,
            @Value("${app.invoicing.serie-boleta:B001}") String serieBoleta,
            @Value("${app.invoicing.serie-factura:F001}") String serieFactura,
            @Value("${app.invoicing.emisor.ruc:}") String emisorRuc,
            @Value("${app.invoicing.emisor.razon-social:}") String emisorRazonSocial,
            @Value("${app.invoicing.emisor.nombre-comercial:}") String emisorNombreComercial,
            @Value("${app.invoicing.emisor.ubigeo:}") String emisorUbigeo,
            @Value("${app.invoicing.emisor.direccion:}") String emisorDireccion) {
        this.apiUrl = apiUrl;
        this.serieBoleta = serieBoleta;
        this.serieFactura = serieFactura;
        this.emisorRuc = emisorRuc;
        this.emisorRazonSocial = emisorRazonSocial;
        this.emisorNombreComercial = emisorNombreComercial;
        this.emisorUbigeo = emisorUbigeo;
        this.emisorDireccion = emisorDireccion;

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
        if (!StringUtils.hasText(emisorRuc)) {
            log.warn("Datos del emisor (EMISOR_RUC, etc.) no configurados — se omite facturación del pedido {}",
                    order.getPublicReference());
            return InvoiceResult.notConfigured();
        }

        boolean isFactura = order.getCustomerDocumentType() == DocumentType.RUC;
        String path = isFactura ? "/api/comprobantes/facturas" : "/api/comprobantes/boletas";

        try {
            JsonNode response = restClient.post()
                    .uri(apiUrl + path)
                    .body(buildRequest(order, isFactura))
                    .retrieve()
                    .body(JsonNode.class);

            String estado = response != null && response.hasNonNull("estado") ? response.get("estado").asText() : null;
            String numeroCompleto = response != null && response.hasNonNull("numeroCompleto")
                    ? response.get("numeroCompleto").asText() : null;

            if (estado != null && estado.toUpperCase(Locale.ROOT).contains("RECHAZ")) {
                String observaciones = response.hasNonNull("observaciones") ? response.get("observaciones").asText() : estado;
                log.error("Comprobante rechazado para pedido {}: {}", order.getPublicReference(), observaciones);
                return InvoiceResult.failure(observaciones);
            }
            if (numeroCompleto == null) {
                log.error("Facturación electrónica: respuesta 2xx sin 'numeroCompleto' para pedido {}: {}",
                        order.getPublicReference(), response);
                return InvoiceResult.failure("El sistema de facturación no devolvió un comprobante válido");
            }
            log.info("Boleta/factura {} emitida para pedido {}", numeroCompleto, order.getPublicReference());
            return InvoiceResult.success(numeroCompleto);
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

    private Map<String, Object> buildRequest(Order order, boolean isFactura) {
        Map<String, Object> emisor = Map.of(
                "ruc", emisorRuc,
                "razonSocial", emisorRazonSocial,
                "nombreComercial", emisorNombreComercial,
                "ubigeo", emisorUbigeo,
                "direccion", emisorDireccion
        );

        Map<String, Object> receptor = Map.of(
                // Catálogo 06 SUNAT (tipo de documento de identidad): 1=DNI, 6=RUC.
                "tipoDocumento", isFactura ? "6" : "1",
                "numeroDocumento", order.getCustomerDocumentNumber(),
                "razonSocialONombres", order.getCustomerName(),
                "direccion", order.getCustomerAddress()
        );

        Map<String, Object> body = new HashMap<>();
        body.put("serie", isFactura ? serieFactura : serieBoleta);
        body.put("emisor", emisor);
        body.put("receptor", receptor);
        body.put("items", toItemPayload(order.getItems()));
        return body;
    }

    private List<Map<String, Object>> toItemPayload(List<OrderItem> items) {
        List<Map<String, Object>> result = new ArrayList<>();
        int numeroItem = 1;
        for (OrderItem item : items) {
            BigDecimal valorUnitario = item.getUnitPrice().divide(ONE_PLUS_IGV, 2, RoundingMode.HALF_UP);
            BigDecimal valorVenta = valorUnitario.multiply(BigDecimal.valueOf(item.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal montoIgv = item.getLineTotal().subtract(valorVenta).setScale(2, RoundingMode.HALF_UP);

            result.add(Map.ofEntries(
                    Map.entry("numeroItem", numeroItem),
                    Map.entry("unidadMedida", "NIU"),
                    Map.entry("cantidad", item.getQuantity()),
                    Map.entry("descripcion", item.getVariantLabel() != null
                            ? item.getProductName() + " (" + item.getVariantLabel() + ")"
                            : item.getProductName()),
                    // Catálogo 07 SUNAT (afectación IGV): 10 = Gravado - Operación Onerosa.
                    Map.entry("tipoAfectacionIgv", "10"),
                    Map.entry("valorUnitario", valorUnitario),
                    Map.entry("valorVenta", valorVenta),
                    Map.entry("montoIgv", montoIgv)
            ));
            numeroItem++;
        }
        return result;
    }
}
