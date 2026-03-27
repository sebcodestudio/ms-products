package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.category.entity.Subcategory;
import com.sebcode.msproducts.category.repository.SubcategoryRepository;
import com.sebcode.msproducts.exception.DuplicateResourceException;
import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.ProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductListResponseDTO;
import com.sebcode.msproducts.product.entity.Brand;
import com.sebcode.msproducts.product.entity.Product;
import com.sebcode.msproducts.product.mapper.ProductMapper;
import com.sebcode.msproducts.product.repository.BrandRepository;
import com.sebcode.msproducts.product.repository.ProductRepository;
import com.sebcode.msproducts.product.service.IProductService;
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
public class ProductServiceImpl implements IProductService {

    private final ProductRepository     productRepository;
    private final BrandRepository       brandRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final ProductMapper         productMapper;

    @Override
    @Transactional
    public ProductDetailResponseDTO createProduct(ProductRequestDTO dto, CustomUserPrincipal principal) {
        String name = dto.getName().trim();
        dto.setName(name);

        productRepository.findByName(name.toLowerCase()).ifPresent(existing -> {
            if (!existing.getIsDeleted()) {
                throw new DuplicateResourceException("Product with name '" + name + "' already exists");
            }
        });

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new NotFoundException("Brand with ID " + dto.getBrandId() + " not found"));

        Product product = productMapper.toEntity(dto);
        product.setBrand(brand);

        resolveAndSetSubcategories(product, dto.getSubcategoryIds());

        Product saved = productRepository.save(product);
        log.info("Product created: '{}' by user: {}", saved.getName(), principal.getId());
        return productMapper.toDetailResponseDTO(saved);
    }

    @Override
    public Optional<ProductDetailResponseDTO> getProductById(Long id) {
        return productRepository.findById(id).map(productMapper::toDetailResponseDTO);
    }

    @Override
    public Page<ProductListResponseDTO> searchAllProducts(String search, Boolean active, int page, int size, String sortBy) {
        log.debug("Executing product search — search={}, active={}", search, active);

        Sort sort = buildSort(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Product> spec = (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
            }
            if (active != null) {
                predicates.add(cb.equal(root.get("state"), active));
                if (active) {
                    predicates.add(cb.equal(root.get("isDeleted"), false));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return productRepository.findAll(spec, pageable).map(productMapper::toListResponseDTO);
    }

    @Override
    @Transactional
    public ProductDetailResponseDTO updateProduct(Long id, ProductRequestDTO dto, CustomUserPrincipal principal) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new NotFoundException("Brand with ID " + dto.getBrandId() + " not found"));

        productMapper.updateEntityFromDTO(dto, product);
        product.setBrand(brand);

        // Reemplazar subcategorías completamente
        product.getSubcategories().clear();
        resolveAndSetSubcategories(product, dto.getSubcategoryIds());

        Product saved = productRepository.save(product);
        log.info("Product updated: '{}' by user: {}", saved.getName(), principal.getId());
        return productMapper.toDetailResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id, CustomUserPrincipal principal) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));

        product.softDelete(principal.getId());
        productRepository.save(product);
        log.info("Product soft deleted: '{}' by user: {}", product.getName(), principal.getId());
    }

    // ─── helpers ────────────────────────────────────────────────────────────

    private void resolveAndSetSubcategories(Product product, List<Long> subcategoryIds) {
        if (subcategoryIds == null || subcategoryIds.isEmpty()) return;

        List<Subcategory> subcategories = subcategoryRepository.findAllById(subcategoryIds);
        if (subcategories.size() != subcategoryIds.size()) {
            List<Long> found = subcategories.stream().map(Subcategory::getId).toList();
            List<Long> missing = subcategoryIds.stream().filter(sid -> !found.contains(sid)).toList();
            throw new NotFoundException("Subcategories not found: " + missing);
        }
        subcategories.forEach(product::addSubcategory);
    }

    private Sort buildSort(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) return Sort.by("id").ascending();
        return switch (sortBy.toLowerCase()) {
            case "name_asc"   -> Sort.by("name").ascending();
            case "name_desc"  -> Sort.by("name").descending();
            case "score_asc"  -> Sort.by("score").ascending();
            case "score_desc" -> Sort.by("score").descending();
            case "id_desc"    -> Sort.by("id").descending();
            default           -> Sort.by("id").ascending();
        };
    }
}