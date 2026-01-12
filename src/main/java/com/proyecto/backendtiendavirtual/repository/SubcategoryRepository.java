package com.proyecto.backendtiendavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyecto.backendtiendavirtual.entity.Subcategory;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    
    @Query("""
        SELECT s FROM Subcategory s
        WHERE s.state = true AND s.idSubcategory = :id
        ORDER BY s.idSubcategory DESC
        """)
    List<Subcategory> findAllCard(Long id, Pageable pageable);

}
