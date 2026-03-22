package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.exception.UnauthorizedException;
import com.sebcode.msproducts.product.dto.request.VariantProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductDetailAdminResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductListAdminResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductDetailPublicResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductListPublicResponseDTO;
import com.sebcode.msproducts.product.entity.AttributeType;
import com.sebcode.msproducts.product.entity.Brand;
import com.sebcode.msproducts.product.entity.Product;
import com.sebcode.msproducts.product.entity.VariantProduct;
import com.sebcode.msproducts.product.mapper.VariantProductMapper;
import com.sebcode.msproducts.product.repository.VariantProductRepository;
import com.sebcode.msproducts.product.service.IVariantProductService;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import com.sun.jdi.request.DuplicateRequestException;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VariantProductServiceImpl implements IVariantProductService {

    private final VariantProductRepository variantProductRepository;
    private final VariantProductMapper variantProductMapper;

    @Override
    @Transactional
    public VariantProductDetailAdminResponseDTO createVariantProduct(VariantProductRequestDTO variantProductRequestDTO, CustomUserPrincipal principal) {
        variantProductRequestDTO.setSku(variantProductRequestDTO.getSku().trim().toLowerCase());
        Optional<VariantProduct> existing = variantProductRepository.findBySku(variantProductRequestDTO.getSku());

        VariantProduct variantProduct;
        if (existing.isPresent() && existing.get().getIsDeleted()) {
            variantProduct = existing.get();
            variantProduct.setIsDeleted(false);
            variantProduct.setDeleteAt(null);
            variantProductMapper.updateEntityFromDTO(variantProductRequestDTO, variantProduct);
        } else if (existing.isPresent()) {
            throw new DuplicateRequestException("Variant product with sku '" + variantProductRequestDTO.getSku() + "' already exists");
        } else {
            variantProduct = variantProductMapper.toEntity(variantProductRequestDTO);
        }

        VariantProduct saved = variantProductRepository.save(variantProduct);
        log.info("Variant product created: {} by admin: {}", variantProductRequestDTO.getSku(), principal.getId());
        return variantProductMapper.toDetailAdminResponseDTO(saved);
    }

    @Override
    public Optional<VariantProductDetailAdminResponseDTO> getVariantProductsByIdAdmin(Long id) {
        return variantProductRepository.findById(id).map(variantProductMapper::toDetailAdminResponseDTO);
    }

    @Override
    public Page<VariantProductListAdminResponseDTO> searchAllVariantProductsAdmin(
            @Nullable String category,
            @Nullable String subcategory,
            @Nullable String brand,
            @Nullable Double score,
            @Nullable String search,
            @Nullable Double minPrice,
            @Nullable Double maxPrice,
            int page, int size,
            @Nullable String sortBy) {

        log.debug("Executing variant product admin search");

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

        Specification<VariantProduct> spec = (root, query, cb) -> {
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
//            if (subcategory != null && !subcategory.isEmpty()) {
//                Join<VariantProduct, Product> productJoin = root.join("product");
//                Join<Product, Subcategory> subcategoryJoin = productJoin.join("subcategories");
//                predicates.add(
//                        cb.equal(cb.lower(subcategoryJoin.get("name")), subcategory.toLowerCase()));
//            }
//            if (category != null && !category.isEmpty()) {
//                Join<VariantProduct, Product> productJoin = root.join("product");
//                Join<Product, Subcategory> subcategoryJoin = productJoin.join("subcategories");
//                Join<Subcategory, Category> categoryJoin = subcategoryJoin.join("category");
//                predicates.add(
//                        cb.equal(cb.lower(categoryJoin.get("name")), category.toLowerCase()));
//            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<VariantProduct> variantProducts = variantProductRepository.findAll(spec, pageable);

        return variantProducts.map(variantProductMapper::toListAdminResponseDTO);
    }

    @Override
    public Optional<VariantProductDetailPublicResponseDTO> getVariantProductsByIdPublic(Long id) {
        return variantProductRepository.findById(id).map(variantProductMapper::toDetailPublicResponseDTO);
    }

    @Override
    public Page<VariantProductListPublicResponseDTO> searchAllVariantProductsPublic(
            @Nullable String category,
            @Nullable String subcategory,
            @Nullable String brand,
            @Nullable Double score,
            @Nullable String search,
            @Nullable Double minPrice,
            @Nullable Double maxPrice,
            int page, int size,
            @Nullable String sortBy) {

        log.debug("Executing variant product public search");

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

        Specification<VariantProduct> spec = (root, query, cb) -> {
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
//            if (subcategory != null && !subcategory.isEmpty()) {
//                Join<VariantProduct, Product> productJoin = root.join("product");
//                Join<Product, Subcategory> subcategoryJoin = productJoin.join("subcategories");
//                predicates.add(
//                        cb.equal(cb.lower(subcategoryJoin.get("name")), subcategory.toLowerCase()));
//            }
//            if (category != null && !category.isEmpty()) {
//                Join<VariantProduct, Product> productJoin = root.join("product");
//                Join<Product, Subcategory> subcategoryJoin = productJoin.join("subcategories");
//                Join<Subcategory, Category> categoryJoin = subcategoryJoin.join("category");
//                predicates.add(
//                        cb.equal(cb.lower(categoryJoin.get("name")), category.toLowerCase()));
//            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<VariantProduct> variantProducts = variantProductRepository.findAll(spec, pageable);

        return variantProducts.map(variantProductMapper::toListPublicResponseDTO);
    }

    @Override
    public List<VariantProductListPublicResponseDTO> findVariantProductsByCategoryId(Long categoryId) {
        return variantProductRepository.findByCategoryId(categoryId).stream()
                .sorted(Comparator.comparing(VariantProduct::getId))
                .map(variantProductMapper::toListPublicResponseDTO)
                .collect(toList());
    }

    @Override
    public List<VariantProductListPublicResponseDTO> findVariantProductsBySubcategoryId(Long subcategoryId) {
        return variantProductRepository.findBySubcategoryId(subcategoryId).stream()
                .sorted(Comparator.comparing(VariantProduct::getId))
                .map(variantProductMapper::toListPublicResponseDTO)
                .collect(toList());
    }

    @Override
    public List<VariantProductListPublicResponseDTO> findBestSellingVariantProducts() {
        return variantProductRepository.findBestSelling(PageRequest.of(0, 5)).stream()
                .map(variantProductMapper::toListPublicResponseDTO)
                .collect(toList());
    }

    @Override
    public List<VariantProductListPublicResponseDTO> findVariantProductsDiscountedByCategoryId(Long categoryId) {
        return variantProductRepository.findDiscountedByCategoryId(categoryId, PageRequest.of(0, 10))
                .stream()
                .map(variantProductMapper::toListPublicResponseDTO)
                .collect(toList());
    }

    @Override
    @Transactional
    public VariantProductDetailAdminResponseDTO updateVariantProduct(Long id, VariantProductRequestDTO variantProductRequestDTO, CustomUserPrincipal principal) {
        variantProductRequestDTO.setSku(variantProductRequestDTO.getSku().trim().toLowerCase());
        VariantProduct variantProduct = variantProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Variant product with ID " + id + " not found"));

        variantProductMapper.updateEntityFromDTO(variantProductRequestDTO, variantProduct);

        VariantProduct saved = variantProductRepository.save(variantProduct);
        log.info("Variant product updated: {} by admin: {}", variantProduct.getSku(), principal.getId());
        return variantProductMapper.toDetailAdminResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteVariantProduct(Long id, CustomUserPrincipal principal) {
        VariantProduct variantProduct = variantProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Variant product with ID " + id + " not found"));

        variantProduct.setIsDeleted(true);
        variantProduct.setDeleteAt(LocalDate.now().atStartOfDay());
        variantProductRepository.save(variantProduct);
        log.info("Variant product soft deleted: {} by admin: {}", variantProduct.getSku(), principal.getId());
    }

}
