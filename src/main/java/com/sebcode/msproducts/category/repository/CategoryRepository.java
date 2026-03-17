package com.sebcode.msproducts.category.repository;

import com.sebcode.msproducts.category.entity.Category;
import com.sebcode.msproducts.product.entity.AttributeType;
import com.sebcode.msproducts.product.entity.AttributeValue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    @Query("""
            SELECT c FROM Category c
            WHERE c.state = true AND c.isDeleted = false
            ORDER BY c.displayOrder DESC
            """)
    List<Category> findAllList(Pageable pageable);

    Optional<Category> findByName(String name);

}
