package com.sebcode.msproducts.category.repository;

import com.sebcode.msproducts.category.entity.Category;
import com.sebcode.msproducts.category.entity.Subcategory;
import com.sebcode.msproducts.product.entity.AttributeValue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long>, JpaSpecificationExecutor<Subcategory> {

    @Query("""
            SELECT s FROM Subcategory s
            JOIN s.category c
            WHERE c.isDeleted = false AND s.state = true AND s.isDeleted = false AND s.id = :subcategoryId
            ORDER BY s.displayOrder DESC
            """)
    List<Subcategory> findAllList(Long subcategoryId, Pageable pageable);

    Optional<Subcategory> findByName(String name);

    Optional<Subcategory> findByNameAndCategoryId(String name, Long categoryId);

}
