package com.sebcode.msproducts.order.repository;

import com.sebcode.msproducts.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByPublicReference(UUID publicReference);

    @Query("""
            SELECT o FROM Order o
            WHERE o.isDeleted = false
            ORDER BY o.createdAt DESC
            """)
    Page<Order> findAllList(Pageable pageable);

}
