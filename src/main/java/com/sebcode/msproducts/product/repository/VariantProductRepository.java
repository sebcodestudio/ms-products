package com.sebcode.msproducts.product.repository;

import com.sebcode.msproducts.product.entity.VariantProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantProductRepository
        extends JpaRepository<VariantProduct, Long>, JpaSpecificationExecutor<VariantProduct> {

    Optional<VariantProduct> findBySku(String sku);

    //            JOIN p.subcategories s
//            JOIN s.category c//            AND c.id = :categoryId
    @Query(value = """
            SELECT v FROM VariantProduct v
            JOIN v.variantAttributes va
            JOIN v.productImages pi
            JOIN v.product p
            WHERE v.state = true AND p.state = true
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findByCategoryId(Long categoryId);

    //            JOIN p.subcategories s//            AND s.id = :subcategoryId
    @Query(value = """
            SELECT v FROM VariantProduct v
            JOIN v.variantAttributes va
            JOIN v.productImages pi
            JOIN v.product p
            WHERE v.state = true AND p.state = true
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findBySubcategoryId(Long subcategoryId);

    @Query("""
            SELECT v FROM VariantProduct v
            JOIN v.variantAttributes va
            JOIN v.productImages pi
            JOIN v.product p
            WHERE v.state = true AND p.state = true
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findBestSelling(Pageable pageable);

    //            JOIN p.subcategories s
//            JOIN s.category c//            AND c.id = :categoryId
    @Query("""
            SELECT v FROM VariantProduct v
            JOIN v.variantAttributes va
            JOIN v.productImages pi
            JOIN v.product p
            WHERE v.state = true AND p.state = true AND v.discount > 0
            AND CURRENT_DATE BETWEEN v.discountStartDate
            AND v.discountEndDate
            ORDER BY v.soldCount DESC
            """)
    List<VariantProduct> findDiscountedByCategoryId(Long categoryId, Pageable pageable);

}
