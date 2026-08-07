package com.sebcode.msproducts.product.repository;

import com.sebcode.msproducts.product.entity.VariantProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantProductRepository
        extends JpaRepository<VariantProduct, Long>, JpaSpecificationExecutor<VariantProduct> {

    Optional<VariantProduct> findBySku(String sku);

    /**
     * Todas las variantes activas de un mismo producto padre.
     * Usado para construir availableVariants y availableAttributes en el detalle público.
     */
    // Nota: no se puede JOIN FETCH "v.variantAttributes" y "product.images" en la misma
    // consulta — Hibernate no permite fetch simultáneo de más de una colección tipo List
    // (MultipleBagFetchException). product.images queda para carga perezosa: se resuelve en
    // una sola query adicional (todas las variantes comparten la misma instancia de Product
    // dentro del contexto de persistencia, así que no hay N+1 por variante).
    @Query("""
            SELECT v FROM VariantProduct v
            LEFT JOIN FETCH v.variantAttributes va
            LEFT JOIN FETCH va.attributeType
            LEFT JOIN FETCH va.attributeValue
            LEFT JOIN FETCH v.product p
            WHERE v.product.id = :productId
              AND v.state = true AND v.isDeleted = false
            ORDER BY v.id ASC
            """)
    List<VariantProduct> findActiveByProductId(@Param("productId") Long productId);

    @Query("""
            SELECT DISTINCT v FROM VariantProduct v
            JOIN v.product p
            JOIN p.subcategories s
            JOIN s.category c
            WHERE v.state = true AND v.isDeleted = false
            AND p.state = true AND p.isDeleted = false
            AND c.id = :categoryId
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("""
            SELECT DISTINCT v FROM VariantProduct v
            JOIN v.product p
            JOIN p.subcategories s
            WHERE v.state = true AND v.isDeleted = false
            AND p.state = true AND p.isDeleted = false
            AND s.id = :subcategoryId
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findBySubcategoryId(@Param("subcategoryId") Long subcategoryId);

    @Query("""
            SELECT v FROM VariantProduct v
            JOIN v.product p
            WHERE v.state = true AND v.isDeleted = false
            AND p.state = true AND p.isDeleted = false
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findBestSelling(Pageable pageable);

    @Query("""
            SELECT DISTINCT v FROM VariantProduct v
            JOIN v.product p
            JOIN p.subcategories s
            JOIN s.category c
            WHERE v.state = true AND v.isDeleted = false
            AND p.state = true AND p.isDeleted = false
            AND v.discount > 0
            AND c.id = :categoryId
            ORDER BY v.discount DESC
            """)
    List<VariantProduct> findDiscountedByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
