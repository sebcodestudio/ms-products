package com.soa.onlinestorebackend.product.service.impl;

import com.soa.onlinestorebackend.category.entity.Category;
import com.soa.onlinestorebackend.category.entity.Subcategory;
import com.soa.onlinestorebackend.exception.ResourceNotFoundException;
import com.soa.onlinestorebackend.exception.UnauthorizedException;
import com.soa.onlinestorebackend.product.dto.request.VariantProductRequestDTO;
import com.soa.onlinestorebackend.product.dto.response.VariantProductCardResponseDTO;
import com.soa.onlinestorebackend.product.entity.Brand;
import com.soa.onlinestorebackend.product.entity.Product;
import com.soa.onlinestorebackend.product.entity.VariantProduct;
import com.soa.onlinestorebackend.product.mapper.VariantProductMapper;
import com.soa.onlinestorebackend.product.repository.VariantProductRepository;
import com.soa.onlinestorebackend.product.service.IVariantProductService;
import com.soa.onlinestorebackend.security.config.security.CustomUserPrincipal;
import com.soa.onlinestorebackend.user.entity.User;
import com.soa.onlinestorebackend.user.repository.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariantProductServiceImpl implements IVariantProductService {

    private final VariantProductRepository variantProductRepository;
    private final UserRepository userRepository;
    private final VariantProductMapper variantProductMapper;

    @Override
    public void createVariantProduct(VariantProductRequestDTO variantProductRequestDTO, CustomUserPrincipal principal) {
        User seller = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        variantProductRepository.save(variantProductMapper.toEntity(variantProductRequestDTO));
        log.info("Variant product created: {} by admin: {}", variantProductRequestDTO.getSku(), seller.getEmail());
    }

    @Override
    public Optional<VariantProductCardResponseDTO> getVariantProductsById(Long id) {
        return variantProductRepository.findById(id).map(variantProductMapper::toResponseCardDTO);
    }

    @Override
    public Page<VariantProductCardResponseDTO> searchAllVariantProducts(
            @Nullable String category,
            @Nullable String subcategory,
            @Nullable String brand,
            @Nullable Double score,
            @Nullable String search,
            @Nullable Double minPrice,
            @Nullable Double maxPrice,
            int page, int size,
            @Nullable String sortBy) {

        log.debug("Executing variant product search");

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

        Specification<VariantProduct> spec;

        spec = (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            // Buscar por nombre
            if (search != null && !search.isEmpty()) {
                Join<VariantProduct, Product> productJoin = root.join("product");
                predicates.add(
                        cb.like(cb.lower(productJoin.get("name")), "%" + search.toLowerCase() + "%"));
            }
            if (score != null && score > 0) {
                Join<VariantProduct, Product> productJoin = root.join("product");
                predicates.add(
                        cb.equal(productJoin.get("score"), score));
            }
            // Filtrar por marca
            if (brand != null && !brand.isEmpty()) {
                Join<VariantProduct, Product> productJoin = root.join("product");
                Join<Product, Brand> brandJoin = productJoin.join("brand");
                predicates.add(
                        cb.equal(cb.lower(brandJoin.get("name")), brand.toLowerCase()));
            }
            // Filtrar por precio máximo
            if (maxPrice != null && maxPrice > 0) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            // Filtrar por precio mínimo
            if (minPrice != null && minPrice > 0) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            // Filtro por categoría (a través de variantProducts -> subcategories -> category)
            if (subcategory != null && !subcategory.isEmpty()) {
                Join<VariantProduct, Product> productJoin = root.join("product");
                Join<Product, Subcategory> subcategoryJoin = productJoin.join("subcategories");
                predicates.add(
                        cb.equal(cb.lower(subcategoryJoin.get("name")), subcategory.toLowerCase()));
            }
            if (category != null && !category.isEmpty()) {
                Join<VariantProduct, Product> productJoin = root.join("product");
                Join<Product, Subcategory> subcategoryJoin = productJoin.join("subcategories");
                Join<Subcategory, Category> categoryJoin = subcategoryJoin.join("category");
                predicates.add(
                        cb.equal(cb.lower(categoryJoin.get("name")), category.toLowerCase()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<VariantProduct> variantProducts = variantProductRepository.findAll(spec, pageable);

        return variantProducts.map(variantProductMapper::toResponseCardDTO);
    }

    @Override
    public List<VariantProductCardResponseDTO> findVariantProductsByCategoryId(Long categoryId) {
        return variantProductRepository.findByCategoryId(categoryId).stream()
                .sorted(Comparator.comparing(VariantProduct::getId))
                .map(variantProductMapper::toResponseCardDTO)
                .collect(toList());
    }

    @Override
    public List<VariantProductCardResponseDTO> findVariantProductsBySubcategoryId(Long subcategoryId) {
        return variantProductRepository.findBySubcategoryId(subcategoryId).stream()
                .sorted(Comparator.comparing(VariantProduct::getId))
                .map(variantProductMapper::toResponseCardDTO)
                .collect(toList());
    }

    @Override
    public List<VariantProductCardResponseDTO> findBestSellingVariantProducts() {
        return variantProductRepository.findBestSelling(PageRequest.of(0, 5)).stream()
                .map(variantProductMapper::toResponseCardDTO)
                .collect(toList());
    }

    @Override
    public List<VariantProductCardResponseDTO> findVariantProductsDiscountedByCategoryId(Long categoryId) {
        return variantProductRepository.findDiscountedByCategoryId(categoryId, PageRequest.of(0, 10))
                .stream()
                .map(variantProductMapper::toResponseCardDTO)
                .collect(toList());
    }

    @Override
    public void updateVariantProduct(Long id, VariantProductRequestDTO variantProductRequestDTO, CustomUserPrincipal principal) {
        User seller = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        VariantProduct variantProduct = variantProductRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("Variant product not found or you don-t have permission"));

        variantProductMapper.updateEntityFromDTO(variantProductRequestDTO, variantProduct);

        variantProductRepository.save(variantProduct);
        log.info("Variant product updated: {}", variantProduct.getSku());
    }

    @Override
    public void deleteVariantProduct(Long id, CustomUserPrincipal principal) {
        User seller = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        VariantProduct variantProduct = variantProductRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("Variant product not found or you don-t have permission"));

        variantProduct.setIsDeleted(true);
        variantProduct.setDeleteDate(LocalDate.now());
        variantProductRepository.save(variantProduct);
        log.info("Variant product soft delete: {}", variantProduct.getSku());
    }

}
