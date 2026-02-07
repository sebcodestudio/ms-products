package com.soa.onlinestorebackend.category.repository;

import com.soa.onlinestorebackend.category.entity.Subcategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    @Query("""
            SELECT s FROM Subcategory s
            WHERE s.state = true AND s.id = :subcategoryId
            ORDER BY s.id DESC
            """)
    List<Subcategory> findAllCard(Long subcategoryId, Pageable pageable);

}
