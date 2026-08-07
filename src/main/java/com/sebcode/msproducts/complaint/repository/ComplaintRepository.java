package com.sebcode.msproducts.complaint.repository;

import com.sebcode.msproducts.complaint.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query("""
            SELECT c FROM Complaint c
            WHERE c.isDeleted = false
            ORDER BY c.createdAt DESC
            """)
    Page<Complaint> findAllList(Pageable pageable);

}
