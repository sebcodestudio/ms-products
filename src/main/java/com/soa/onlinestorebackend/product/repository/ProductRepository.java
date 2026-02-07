package com.soa.onlinestorebackend.product.repository;

import com.soa.onlinestorebackend.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("""
            SELECT p FROM Product p
            WHERE p.state = true
            ORDER BY p.score DESC
            """)
    List<Product> findBySold(Pageable pageable);

    @Query("""
            SELECT p FROM Product p
            LEFT JOIN p.subcategories s
            LEFT JOIN s.category c
            WHERE  p.state = true AND c.id = :categoryId
            ORDER BY p.score DESC
            """)
    List<Product> findByIdCategory(Long categoryId, Pageable pageable);

    @Query("""
            SELECT p FROM Product p
            LEFT JOIN p.variantProducts v
            LEFT JOIN p.subcategories s
            LEFT JOIN s.category c
            WHERE  p.state = true AND v.discount > 0
            AND CURRENT_DATE BETWEEN v.discountStartDate
            AND v.discountEndDate AND c.id = :categoryId
            ORDER BY p.score DESC
            """)
    List<Product> findDiscountByIdCategory(Long categoryId, Pageable pageable);

}
