package com.sebcode.msproducts.order.payment;

import com.sebcode.msproducts.order.entity.PaymentProviderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

/**
 * Simula un cobro sin llamar a Culqi — permite validar el flujo completo
 * (crear pedido -> pagar -> facturación) mientras no haya llaves de prueba
 * reales. Se activa con app.payment.provider=mock; es el default en
 * application-dev.yml, así que corre local sin configuración extra.
 * <p>
 * Convención de tokens para probar ambos caminos desde el checkout: un token
 * que contenga "fail" simula un cobro rechazado, cualquier otro simula éxito.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.payment", name = "provider", havingValue = "mock")
public class MockPaymentProvider implements PaymentProvider {

    @Override
    public PaymentProviderType getType() {
        return PaymentProviderType.MOCK;
    }

    @Override
    public ChargeResult charge(BigDecimal amount, String currency, String token, String description, String customerEmail) {
        log.warn("[PAGO SIMULADO] {} {} — token '{}' — {}", amount, currency, token, description);

        if (token == null || token.isBlank()) {
            return ChargeResult.failure("Token de pago vacío (simulado)");
        }
        if (token.toLowerCase(Locale.ROOT).contains("fail")) {
            return ChargeResult.failure("Tarjeta rechazada (simulado — token de prueba)");
        }
        return ChargeResult.success("mock_" + UUID.randomUUID());
    }
}
