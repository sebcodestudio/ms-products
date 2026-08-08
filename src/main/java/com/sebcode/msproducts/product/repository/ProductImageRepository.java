package com.sebcode.msproducts.product.repository;

import com.sebcode.msproducts.product.entity.AttributeValue;
import com.sebcode.msproducts.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>, JpaSpecificationExecutor<ProductImage> {
    Optional<ProductImage> findByImageUrlAndProductIdAndAttributeValueId(String value, Long productId, Long attributeValueId);
    Optional<ProductImage> findByImageUrlAndProductIdAndAttributeValueIsNull(String value, Long productId);
    Optional<ProductImage> findByImageUrl(String imageUrl);
    boolean existsByProductIdAndIsMainTrue(Long productId);
}
