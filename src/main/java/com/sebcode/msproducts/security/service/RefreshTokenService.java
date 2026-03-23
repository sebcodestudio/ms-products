package com.sebcode.msproducts.security.service;

import com.sebcode.msproducts.exception.UnauthorizedException;
import com.sebcode.msproducts.security.entity.TokenRecuperation;
import com.sebcode.msproducts.security.repository.TokenRecuperationRepository;
import com.sebcode.msproducts.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_EXPIRATION_DAYS = 7;

    private final TokenRecuperationRepository tokenRecuperationRepository;

    @Transactional
    public TokenRecuperation createRefreshToken(User user) {
        TokenRecuperation refreshToken = TokenRecuperation.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expirationDate(LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRATION_DAYS))
                .isUsed(false)
                .build();

        return tokenRecuperationRepository.save(refreshToken);
    }

    @Transactional
    public TokenRecuperation validateAndConsume(String token) {
        TokenRecuperation refreshToken = tokenRecuperationRepository
                .findByTokenAndIsUsedFalse(token)
                .orElseThrow(() -> new UnauthorizedException("Refresh token invalido o ya utilizado"));

        if (refreshToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh token expirado, inicia sesion nuevamente");
        }

        refreshToken.setIsUsed(true);
        refreshToken.setUsedDate(LocalDateTime.now());
        tokenRecuperationRepository.save(refreshToken);

        return refreshToken;
    }

    // Limpieza automatica de tokens expirados cada 24 horas
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanExpiredTokens() {
        tokenRecuperationRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Expired refresh tokens cleaned up");
    }
}