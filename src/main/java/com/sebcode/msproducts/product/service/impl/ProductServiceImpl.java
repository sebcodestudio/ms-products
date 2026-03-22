package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.exception.DuplicateResourceException;
import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.ProductRequestDTO;
import com.sebcode.msproducts.product.dto.request.ProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductListResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductDetailResponseDTO;
import com.sebcode.msproducts.product.entity.Product;
import com.sebcode.msproducts.product.entity.Product;
import com.sebcode.msproducts.product.mapper.ProductMapper;
import com.sebcode.msproducts.product.mapper.ProductMapper;
import com.sebcode.msproducts.product.repository.ProductRepository;
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

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDetailResponseDTO createProduct(ProductRequestDTO productRequestDTO, CustomUserPrincipal principal) {
        productRequestDTO.setName(productRequestDTO.getName().trim().toLowerCase());
        Optional<Product> existing = productRepository.findByName(productRequestDTO.getName());

        Product product;
        if (existing.isPresent() && existing.get().getIsDeleted()) {
            product = existing.get();
            product.setIsDeleted(false);
            product.setDeleteAt(null);
            productMapper.updateEntityFromDTO(productRequestDTO, product);
        } else if (existing.isPresent()) {
            throw new DuplicateResourceException("Product with name '" + productRequestDTO.getName() + "' already exists");
        } else {
            product = productMapper.toEntity(productRequestDTO);
        }

        Product saved = productRepository.save(product);
        log.info("Product created: {} by admin: {}", productRequestDTO.getName(), principal.getId());
        return productMapper.toDetailResponseDTO(saved);
    }

    @Override
    public Optional<ProductDetailResponseDTO> getProductById(Long id) {
        return productRepository.findById(id).map(productMapper::toDetailResponseDTO);
    }

    @Override
    public Page<ProductListResponseDTO> searchAllProducts(String search, Boolean isVisual, int page, int size, String sortBy) {
        log.debug("Executing product search");

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

        Specification<Product> spec = (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            // Buscar por nombre
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
            }
            if (isVisual != null) {
                predicates.add(
                        cb.equal(root.get("isVisual"), isVisual));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return productRepository.findAll(spec, pageable).map(productMapper::toListResponseDTO);
    }

    @Override
    @Transactional
    public ProductDetailResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO, CustomUserPrincipal principal) {
        productRequestDTO.setName(productRequestDTO.getName().trim().toLowerCase());
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));

        productMapper.updateEntityFromDTO(productRequestDTO, product);

        Product saved = productRepository.save(product);
        log.info("Product updated: {} by admin: {}", product.getName(), principal.getId());
        return productMapper.toDetailResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id, CustomUserPrincipal principal) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));

        product.setIsDeleted(true);
        product.setDeleteAt(LocalDate.now().atStartOfDay());
        productRepository.save(product);
        log.info("Product soft deleted: {} by admin: {}", product.getName(), principal.getId());
    }

}
