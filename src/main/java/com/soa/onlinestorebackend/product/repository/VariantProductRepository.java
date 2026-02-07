package com.soa.onlinestorebackend.product.repository;

import com.soa.onlinestorebackend.product.entity.VariantProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantProductRepository
        extends JpaRepository<VariantProduct, Long>, JpaSpecificationExecutor<VariantProduct> {

    @Query(value = """
            SELECT v FROM VariantProduct v
            JOIN v.variantAttributes va
            JOIN va.productImgs pi
            JOIN v.product p
            JOIN p.subcategories s
            JOIN s.category c
            WHERE v.state = true AND p.state = true AND c.id = :categoryId
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findByCategoryId(Long categoryId);

    @Query(value = """
            SELECT v FROM VariantProduct v
            JOIN v.variantAttributes va
            JOIN va.productImgs pi
            JOIN v.product p
            JOIN p.subcategories s
            WHERE v.state = true AND p.state = true AND s.id = :subcategoryId
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findBySubcategoryId(Long subcategoryId);

    @Query("""
            SELECT v FROM VariantProduct v
            JOIN v.variantAttributes va
            JOIN va.productImgs pi
            JOIN v.product p
            WHERE v.state = true AND p.state = true
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findBestSelling(Pageable pageable);

    @Query("""
            SELECT v FROM VariantProduct v
            JOIN v.variantAttributes va
            JOIN va.productImgs pi
            JOIN v.product p
            JOIN p.subcategories s
            JOIN s.category c
            WHERE v.state = true AND p.state = true AND v.discount > 0
            AND CURRENT_DATE BETWEEN v.discountStartDate
            AND v.discountEndDate AND c.id = :categoryId
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findDiscountedByCategoryId(Long categoryId, Pageable pageable);

}
