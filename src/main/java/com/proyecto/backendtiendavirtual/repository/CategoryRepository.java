package com.proyecto.backendtiendavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyecto.backendtiendavirtual.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    @Query("""
        SELECT c FROM Category c
        WHERE c.state = true
        ORDER BY c.idCategory DESC
        """)
    List<Category> findAllCard(Pageable pageable);

}
