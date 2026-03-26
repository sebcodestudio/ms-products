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
    List<VariantProduct> findByCategoryId(@Param("categoryId") Long categoryId);

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