package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.exception.DuplicateResourceException;
import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.ProductImageRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductImageDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductImageListResponseDTO;
import com.sebcode.msproducts.product.entity.ProductImage;
import com.sebcode.msproducts.product.mapper.ProductImageMapper;
import com.sebcode.msproducts.product.repository.AttributeValueRepository;
import com.sebcode.msproducts.product.repository.ProductImageRepository;
import com.sebcode.msproducts.product.repository.VariantProductRepository;
import com.sebcode.msproducts.product.service.IProductImageService;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductImageServiceImpl implements IProductImageService {

    private final ProductImageRepository productImageRepository;
    private final VariantProductRepository variantProductRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final ProductImageMapper productImageMapper;

    @Override
    @Transactional
    public ProductImageDetailResponseDTO createProductImage(ProductImageRequestDTO productImageRequestDTO, CustomUserPrincipal principal) {
//        productImageRequestDTO.setValue(productImageRequestDTO.getImageUrl().trim().toLowerCase());
//        Optional<ProductImage> existing = productImageRepository.findByValue(productImageRequestDTO.getImageUrl());

        variantProductRepository.findById(productImageRequestDTO.getVariantProductId())
                .orElseThrow(() -> new NotFoundException("Product image with id '" + productImageRequestDTO.getVariantProductId() + "' don't exists"));
        attributeValueRepository.findById(productImageRequestDTO.getAttributeValueId())
                .orElseThrow(() -> new NotFoundException("Product image with id '" + productImageRequestDTO.getVariantProductId() + "' don't exists"));

        Optional<ProductImage> existing = productImageRepository
                .findByImageUrlAndVariantProductIdAndAttributeValueId(productImageRequestDTO.getImageUrl(), productImageRequestDTO.getVariantProductId(), productImageRequestDTO.getAttributeValueId());

        ProductImage productImage;
        if (existing.isPresent() && existing.get().getIsDeleted()) {
            productImage = existing.get();
            productImage.setIsDeleted(false);
            productImage.setDeleteAt(null);
            productImageMapper.updateEntityFromDTO(productImageRequestDTO, productImage);
        } else if (existing.isPresent()) {
            throw new DuplicateResourceException("Product image with name '" + productImageRequestDTO.getImageUrl() + "' already exists");
        } else {
            productImage = productImageMapper.toEntity(productImageRequestDTO);
        }

        ProductImage saved = productImageRepository.save(productImage);
        log.info("Product image created: {} by admin: {}", productImageRequestDTO.getImageUrl(), principal.getId());
        return productImageMapper.toDetailResponseDTO(saved);
    }

    @Override
    public Optional<ProductImageDetailResponseDTO> getProductImageById(Long id) {
        return productImageRepository.findById(id).map(productImageMapper::toDetailResponseDTO);
    }

    @Override
    public Page<ProductImageListResponseDTO> searchAllProductImages(String search, Boolean isVisual, int page, int size, String sortBy) {
        log.debug("Executing product image search");

        String valAsc = "Asc";
        String valDesc = "Desc";
        String sortField = "";
        String sortDirection = "";

        if (sortBy != null) {
            if (sortBy.contains(valAsc)) {
                sortField = sortBy.replace(valAsc, "");
                sortDirection = valAsc;
            }
            if (sortBy.contains(valDesc)) {
                sortField = sortBy.replace(valDesc, "");
                sortDirection = valDesc;
            }
        }
        if (sortField.isEmpty()) {
            sortField = "id";
        }
        Sort sort = sortDirection.equalsIgnoreCase(valDesc) ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<ProductImage> spec = (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            // Buscar por nombre
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        cb.like(cb.lower(root.get("value")), "%" + search.toLowerCase() + "%"));
            }
            if (isVisual != null) {
                predicates.add(
                        cb.equal(root.get("isVisual"), isVisual));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return productImageRepository.findAll(spec, pageable).map(productImageMapper::toListResponseDTO);
    }

    @Override
    @Transactional
    public ProductImageDetailResponseDTO updateProductImage(Long id, ProductImageRequestDTO productImageRequestDTO, CustomUserPrincipal principal) {
//        productImageRequestDTO.setImageUrl(productImageRequestDTO.getImageUrl().trim().toLowerCase());
        variantProductRepository.findById(productImageRequestDTO.getVariantProductId())
                .orElseThrow(() -> new NotFoundException("Product image with id '" + productImageRequestDTO.getVariantProductId() + "' don't exists"));
        attributeValueRepository.findById(productImageRequestDTO.getAttributeValueId())
                .orElseThrow(() -> new NotFoundException("Product image with id '" + productImageRequestDTO.getVariantProductId() + "' don't exists"));

        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product image with ID " + id + " not found"));

        productImageMapper.updateEntityFromDTO(productImageRequestDTO, productImage);

        ProductImage saved = productImageRepository.save(productImage);
        log.info("Product image updated: {} by admin: {}", productImage.getImageUrl(), principal.getId());
        return productImageMapper.toDetailResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteProductImage(Long id, CustomUserPrincipal principal) {
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product image with ID " + id + " not found"));

        productImage.setIsDeleted(true);
        productImage.setDeleteAt(LocalDate.now().atStartOfDay());
        productImageRepository.save(productImage);
        log.info("Product image soft deleted: {} by admin: {}", productImage.getImageUrl(), principal.getId());
    }

}
