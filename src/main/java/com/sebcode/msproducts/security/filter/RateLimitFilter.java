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

    private final Map<String, Bucket> loginBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> publicBuckets = new ConcurrentHashMap<>();

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
            Bucket bucket = publicBuckets.computeIfAbsent(ip, k -> buildBucket(PUBLIC_LIMIT, PUBLIC_WINDOW));
            if (!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded for public endpoint from IP: {}", ip);
                writeRateLimitResponse(response, "Demasiadas solicitudes. Intenta en 1 minuto.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Bucket buildBucket(int limit, Duration window) {
        Bandwidth bandwidth = Bandwidth.classic(limit, Refill.greedy(limit, window));
        return Bucket.builder().addLimit(bandwidth).build();
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