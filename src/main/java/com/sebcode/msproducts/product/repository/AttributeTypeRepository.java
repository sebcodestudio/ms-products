package com.sebcode.msproducts.product.repository;

import com.sebcode.msproducts.product.entity.AttributeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeTypeRepository extends JpaRepository<AttributeType, Long>, JpaSpecificationExecutor<AttributeType> {
    Optional<AttributeType> findByName(String name);
}
