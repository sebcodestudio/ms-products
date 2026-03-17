package com.sebcode.msproducts.product.repository;

import com.sebcode.msproducts.product.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long>, JpaSpecificationExecutor<AttributeValue> {
    //    Optional<AttributeValue> findByValue(String value);
    Optional<AttributeValue> findByValueAndAttributeTypeId(String value, Long attributeTypeId);
}
