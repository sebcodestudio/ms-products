package com.sebcode.msproducts.security.repository;

import com.sebcode.msproducts.security.entity.TokenRecuperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRecuperationRepository extends JpaRepository<TokenRecuperation, Long> {

    Optional<TokenRecuperation> findByTokenAndIsUsedFalse(String token);

    @Modifying
    @Query("DELETE FROM TokenRecuperation t WHERE t.expirationDate < :now")
    void deleteExpiredTokens(LocalDateTime now);
}