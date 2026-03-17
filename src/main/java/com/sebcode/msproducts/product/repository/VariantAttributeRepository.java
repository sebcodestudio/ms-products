package com.sebcode.msproducts.product.repository;

import com.sebcode.msproducts.product.entity.AttributeType;
import com.sebcode.msproducts.product.entity.VariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, Long>, JpaSpecificationExecutor<VariantAttribute> {
}
