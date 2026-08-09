package com.sebcode.msproducts.order.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.sebcode.msproducts.order.entity.PaymentProviderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Cobra usando la API de cargos de Culqi (https://api.culqi.com/v2/charges).
 * El "token" ya viene generado del lado del cliente por Culqi Checkout — acá
 * solo se usa para efectivamente cobrar, con la llave secreta.
 */
@Slf4j
@Component
public class CulqiPaymentProvider implements PaymentProvider {

    private static final String CHARGES_URL = "https://api.culqi.com/v2/charges";

    private final RestClient restClient;

    public CulqiPaymentProvider(@Value("${app.payment.culqi.secret-key}") String secretKey) {
        this.restClient = RestClient.builder()
                .defaultHeader("Authorization", "Bearer " + secretKey)
                .build();
    }

    @Override
    public PaymentProviderType getType() {
        return PaymentProviderType.CULQI;
    }

    @Override
    public ChargeResult charge(BigDecimal amount, String currency, String token, String description, String customerEmail) {
        // Culqi espera el monto en centimos (ej. S/ 49.90 -> 4990).
        long amountInCents = amount.setScale(2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        Map<String, Object> body = Map.of(
                "amount", amountInCents,
                "currency_code", currency,
                "email", customerEmail != null ? customerEmail : "",
                "source_id", token,
                "description", description
        );

        try {
            JsonNode response = restClient.post()
                    .uri(CHARGES_URL)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);

            String chargeId = response != null && response.has("id") ? response.get("id").asText() : null;
            if (chargeId == null) {
                log.error("Culqi devolvio 2xx sin 'id' de cargo: {}", response);
                return ChargeResult.failure("La pasarela no devolvio un cargo valido");
            }
            return ChargeResult.success(chargeId);
        } catch (RestClientResponseException e) {
            String reason = extractErrorMessage(e);
            log.warn("Cargo Culqi rechazado: {} — {}", e.getStatusCode(), reason);
            return ChargeResult.failure(reason);
        } catch (Exception e) {
            log.error("Fallo de conectividad con Culqi", e);
            return ChargeResult.failure("No se pudo conectar con la pasarela de pago");
        }
    }

    private String extractErrorMessage(RestClientResponseException e) {
        try {
            JsonNode error = e.getResponseBodyAs(JsonNode.class);
            if (error != null && error.has("user_message")) {
                return error.get("user_message").asText();
            }
            if (error != null && error.has("merchant_message")) {
                return error.get("merchant_message").asText();
            }
        } catch (Exception ignored) {
            // body no parseable como JSON, seguimos con el mensaje generico
        }
        return "La pasarela rechazo el pago";
    }
}
