package com.sebcode.msproducts.security.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Limite para login: 10 intentos por minuto por IP
    private static final int LOGIN_LIMIT = 10;
    private static final Duration LOGIN_WINDOW = Duration.ofMinutes(1);

    // Limite para endpoints publicos: 100 requests por minuto por IP
    private static final int PUBLIC_LIMIT = 100;
    private static final Duration PUBLIC_WINDOW = Duration.ofMinutes(1);

    // Limite para trafico autenticado (admin) sobre variant-products: separado del
    // publico para que ediciones/importaciones masivas del panel admin no compitan
    // por el mismo balde que las visitas anonimas de la tienda desde la misma IP.
    private static final int ADMIN_WRITE_LIMIT = 300;
    private static final Duration ADMIN_WRITE_WINDOW = Duration.ofMinutes(1);

    // Limite para registrar reclamos/quejas: 5 por minuto por IP (evita spam/abuso del envio de emails)
    private static final int COMPLAINTS_LIMIT = 5;
    private static final Duration COMPLAINTS_WINDOW = Duration.ofMinutes(1);

    // Limite para crear pedidos / intentar pagos: 10 por minuto por IP (evita
    // spam de pedidos falsos o intentos repetidos de cobro con tokens invalidos)
    private static final int ORDERS_WRITE_LIMIT = 10;
    private static final Duration ORDERS_WRITE_WINDOW = Duration.ofMinutes(1);

    private final Map<String, Bucket> loginBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> publicBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> adminWriteBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> complaintsBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> ordersWriteBuckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String ip = resolveClientIp(request);

        if (path.startsWith("/api/v1/auth/login") || path.startsWith("/api/v1/auth/refresh-token")) {
            Bucket bucket = loginBuckets.computeIfAbsent(ip, k -> buildBucket(LOGIN_LIMIT, LOGIN_WINDOW));
            if (!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded for login from IP: {}", ip);
                writeRateLimitResponse(response, "Demasiados intentos de login. Intenta en 1 minuto.");
                return;
            }
        } else if (path.startsWith("/api/v1/variant-products")) {
            // Trafico con Authorization (panel admin: altas/ediciones, carga masiva) usa
            // un balde propio y mas amplio; el resto (navegacion publica de la tienda)
            // sigue en el balde publico de siempre.
            boolean isAuthenticated = hasBearerToken(request);
            Bucket bucket = isAuthenticated
                    ? adminWriteBuckets.computeIfAbsent(ip, k -> buildBucket(ADMIN_WRITE_LIMIT, ADMIN_WRITE_WINDOW))
                    : publicBuckets.computeIfAbsent(ip, k -> buildBucket(PUBLIC_LIMIT, PUBLIC_WINDOW));
            if (!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded for {} endpoint from IP: {}", isAuthenticated ? "admin" : "public", ip);
                writeRateLimitResponse(response, "Demasiadas solicitudes. Intenta en 1 minuto.");
                return;
            }
        } else if (path.startsWith("/api/v1/complaints") && "POST".equalsIgnoreCase(request.getMethod())) {
            Bucket bucket = complaintsBuckets.computeIfAbsent(ip, k -> buildBucket(COMPLAINTS_LIMIT, COMPLAINTS_WINDOW));
            if (!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded for complaints endpoint from IP: {}", ip);
                writeRateLimitResponse(response, "Demasiados reclamos enviados. Intenta en 1 minuto.");
                return;
            }
        } else if (path.startsWith("/api/v1/orders") && "POST".equalsIgnoreCase(request.getMethod())) {
            Bucket bucket = ordersWriteBuckets.computeIfAbsent(ip, k -> buildBucket(ORDERS_WRITE_LIMIT, ORDERS_WRITE_WINDOW));
            if (!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded for orders endpoint from IP: {}", ip);
                writeRateLimitResponse(response, "Demasiadas solicitudes de pedido. Intenta en 1 minuto.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Bucket buildBucket(int limit, Duration window) {
        Bandwidth bandwidth = Bandwidth.classic(limit, Refill.greedy(limit, window));
        return Bucket.builder().addLimit(bandwidth).build();
    }

    private boolean hasBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header != null && header.regionMatches(true, 0, "Bearer ", 0, 7);
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private void writeRateLimitResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"status\":429,\"code\":\"RATE_LIMIT_EXCEEDED\",\"error\":\"" + message + "\"}"
        );
    }
}