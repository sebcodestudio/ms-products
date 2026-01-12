package com.proyecto.backendtiendavirtual.repository;

import com.proyecto.backendtiendavirtual.entity.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
        @EntityGraph(attributePaths = {
                        "variantProducts"
        })
        Page<Product> findAll(Specification<Product> spec, Pageable pageable);

        @Query("""
                        SELECT p FROM Product p
                        WHERE p.state = true
                        ORDER BY p.soldCount DESC
                        """)
        List<Product> findBySold(Pageable pageable);

        @Query("""
                        SELECT p FROM Product p
                        LEFT JOIN p.subcategories s
                        LEFT JOIN s.category c
                        WHERE  p.state = true AND c.idCategory = :idcat
                        ORDER BY p.soldCount DESC
                        """)
        List<Product> findByIdCategory(Long idcat, Pageable pageable);

        @Query("""
                        SELECT p FROM Product p
                        LEFT JOIN p.variantProducts v
                        LEFT JOIN p.subcategories s
                        LEFT JOIN s.category c
                        WHERE  p.state = true AND v.discount > 0
                        AND CURRENT_DATE BETWEEN v.discountStartDate
                        AND v.discountEndDate AND c.idCategory = :idcat
                        ORDER BY p.soldCount DESC
                        """)
        List<Product> findDiscountByIdCategory(Long idcat, Pageable pageable);

}
