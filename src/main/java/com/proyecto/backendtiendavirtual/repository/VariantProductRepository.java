package com.proyecto.backendtiendavirtual.repository;

import com.proyecto.backendtiendavirtual.entity.VariantProduct;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
// import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantProductRepository
                extends JpaRepository<VariantProduct, Long>, JpaSpecificationExecutor<VariantProduct> {
        // @EntityGraph(attributePaths = {
        // // "variantProducts"
        // })
        // @Query("""
        // SELECT v FROM VariantProduct v
        // JOIN v.product p
        // WHERE v.state = true AND p.state = true
        // ORDER BY p.soldCount DESC
        // """)
        Page<VariantProduct> findAll(Specification<VariantProduct> spec, Pageable pageable);

        @Query("""
                        SELECT v FROM VariantProduct v
                        JOIN v.variantAttributes va
                        JOIN va.productImgs pi
                        JOIN v.product p
                        WHERE v.state = true AND p.state = true
                        ORDER BY p.soldCount DESC
                        """)
        List<VariantProduct> findBySold(Pageable pageable);

        // JOIN v.variantAttributes va
        // JOIN va.productImgs pi
        // JOIN v.product p
        // JOIN p.subcategories s
        // JOIN s.category c
        // WHERE v.state = true AND p.state = true AND c.idCategory = :idcat
        // ORDER BY p.soldCount DESC , nativeQuery = true
        @Query(value = """
                        SELECT v FROM VariantProduct v
                        JOIN v.variantAttributes va
                        JOIN va.productImgs pi
                        JOIN v.product p
                        JOIN p.subcategories s
                        JOIN s.category c
                        WHERE v.state = true AND p.state = true AND c.idCategory = :idcat
                        ORDER BY p.soldCount DESC
                                        """)
        List<VariantProduct> findByIdCategory(Long idcat);

        // SELECT DISTINCT v FROM VariantProduct v
        // JOIN v.variantAttributes va
        // JOIN va.productImgs pi
        // JOIN v.product p
        // JOIN p.subcategories s
        // JOIN s.category c
        // WHERE v.state = true AND p.state = true AND v.discount > 0
        // AND CURRENT_DATE BETWEEN v.discountStartDate
        // AND v.discountEndDate AND c.idCategory = :idcat
        // ORDER BY p.soldCount DESC
        @Query("""
                        SELECT v FROM VariantProduct v
                        JOIN v.variantAttributes va
                        JOIN va.productImgs pi
                        JOIN v.product p
                        JOIN p.subcategories s
                        JOIN s.category c
                        WHERE v.state = true AND p.state = true AND v.discount > 0
                        AND CURRENT_DATE BETWEEN v.discountStartDate
                        AND v.discountEndDate AND c.idCategory = :idcat
                        ORDER BY p.soldCount DESC
                                                            """)
        List<VariantProduct> findDiscountByIdCategory(Long idcat, Pageable pageable);

        // SELECT v FROM VariantProduct v
        // JOIN v.product p
        // JOIN p.subcategories s
        // JOIN s.category c
        // WHERE v.state = true
        // AND p.state = true
        // AND v.discount > 0
        // AND CURRENT_DATE BETWEEN v.discountStartDate AND v.discountEndDate
        // AND c.idCategory = :idcat
        // AND EXISTS (
        // SELECT 1 FROM VariantAttribute va
        // JOIN va.productImgs pi
        // WHERE va.variantProduct = v
        // )
        // ORDER BY p.soldCount DESC

}
