package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.VariantProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductDetailAdminResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductDetailPublicResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductDetailPublicResponseDTO.AttributeOptionDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductDetailPublicResponseDTO.VariantOptionDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductListPublicResponseDTO;
import com.sebcode.msproducts.category.entity.Category;
import com.sebcode.msproducts.category.entity.Subcategory;
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
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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
    public VariantProductDetailAdminResponseDTO createVariantProduct(
            VariantProductRequestDTO variantProductRequestDTO, CustomUserPrincipal principal) {
        variantProductRequestDTO.setSku(variantProductRequestDTO.getSku().trim().toLowerCase());
        Optional<VariantProduct> existing = variantProductRepository.findBySku(variantProductRequestDTO.getSku());

        VariantProduct variantProduct;
        if (existing.isPresent() && existing.get().getIsDeleted()) {
            variantProduct = existing.get();
            variantProduct.setIsDeleted(false);
            variantProduct.setDeleteAt(null);
            variantProductMapper.updateEntityFromDTO(variantProductRequestDTO, variantProduct);
        } else if (existing.isPresent()) {
            throw new DuplicateRequestException(
                    "Variant product with sku '" + variantProductRequestDTO.getSku() + "' already exists");
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
    public Page<VariantProductDetailAdminResponseDTO> searchAllVariantProductsAdmin(
            @Nullable String category, @Nullable String subcategory, @Nullable String brand,
            @Nullable Double score, @Nullable String search, @Nullable Double minPrice,
            @Nullable Double maxPrice, int page, int size, @Nullable String sortBy) {

        log.debug("Executing variant product admin search");
        Pageable pageable = buildPageable(page, size, sortBy);
        Specification<VariantProduct> spec = buildSpec(category, subcategory, search, brand, score, minPrice, maxPrice);
        return variantProductRepository.findAll(spec, pageable).map(variantProductMapper::toDetailAdminResponseDTO);
    }

    // ─── PUBLIC DETAIL ────────────────────────────────────────────────────────

    @Override
    public Optional<VariantProductDetailPublicResponseDTO> getVariantProductsByIdPublic(Long id) {
        return variantProductRepository.findById(id)
                .map(this::toDetailPublicWithSiblings);
    }

    /**
     * Construye el DTO de detalle enriquecido con todas las variantes hermanas
     * del mismo producto padre para permitir la selección de variantes en el frontend.
     */
    private VariantProductDetailPublicResponseDTO toDetailPublicWithSiblings(VariantProduct variant) {
        VariantProductDetailPublicResponseDTO dto = variantProductMapper.toDetailPublicResponseDTO(variant);

        if (variant.getProduct() == null) return dto;

        // Obtener todas las variantes activas del mismo producto padre (incluye atributos e imágenes)
        List<VariantProduct> siblings = variantProductRepository
                .findActiveByProductId(variant.getProduct().getId());

        // Construir availableVariants (todas las variantes del producto)
        List<VariantOptionDTO> availableVariants = siblings.stream()
                .map(s -> VariantOptionDTO.builder()
                        .variantId(s.getId())
                        .sku(s.getSku())
                        .attributes(s.getAttributesMap())
                        .price(s.getPrice())
                        .finalPrice(s.getFinalPrice())
                        .inStock(s.getStock() != null && s.getStock() > 0 && Boolean.TRUE.equals(s.getState()))
                        // Resuelto por el atributo visual (Color) de cada hermano — así el selector
                        // cambia de foto solo cuando cambia Color, no cuando cambia Talla.
                        .mainImageUrl(s.getResolvedMainImageUrl())
                        .build())
                .collect(toList());
        dto.setAvailableVariants(availableVariants);

        // Construir availableAttributes agrupados por tipo de atributo
        // Ejemplo: {"Color": [{"value":"Rojo","available":true,"selected":false}, ...], "Talla": [...]}
        Map<String, String> currentAttrs = variant.getAttributesMap();
        Map<String, List<AttributeOptionDTO>> availableAttributes = new LinkedHashMap<>();

        siblings.stream()
                .flatMap(s -> s.getVariantAttributes().stream())
                .forEach(va -> {
                    String typeName = va.getAttributeType() != null ? va.getAttributeType().getName() : null;
                    String value    = va.getAttributeValue() != null ? va.getAttributeValue().getValue() : null;
                    Long   valId    = va.getAttributeValue() != null ? va.getAttributeValue().getId() : null;
                    if (typeName == null || value == null) return;

                    availableAttributes.computeIfAbsent(typeName, k -> new ArrayList<>());
                    List<AttributeOptionDTO> opts = availableAttributes.get(typeName);

                    boolean alreadyAdded = opts.stream().anyMatch(o -> value.equals(o.getValue()));
                    if (alreadyAdded) return;

                    // ¿Alguna variante con este valor tiene stock?
                    boolean hasStock = siblings.stream()
                            .filter(s -> value.equals(s.getAttributeValue(typeName)))
                            .anyMatch(s -> s.getStock() != null && s.getStock() > 0
                                    && Boolean.TRUE.equals(s.getState()));

                    boolean selected = value.equals(currentAttrs.get(typeName));

                    opts.add(AttributeOptionDTO.builder()
                            .attributeValueId(valId)
                            .value(value)
                            .available(hasStock)
                            .selected(selected)
                            .build());
                });
        dto.setAvailableAttributes(availableAttributes);

        return dto;
    }

    // ─── PUBLIC LIST ──────────────────────────────────────────────────────────

    @Override
    public Page<VariantProductListPublicResponseDTO> searchAllVariantProductsPublic(
            @Nullable String category, @Nullable String subcategory, @Nullable String brand,
            @Nullable Double score, @Nullable String search, @Nullable Double minPrice,
            @Nullable Double maxPrice, int page, int size, @Nullable String sortBy) {

        log.debug("Executing variant product public search");
        Pageable pageable = buildPageable(page, size, sortBy);
        // El catálogo público muestra UN card por producto (no por SKU) — cada color/talla
        // es una fila en variant_products, así que sin esto el mismo diseño aparecería
        // repetido una vez por cada combinación de atributos.
        Specification<VariantProduct> spec = buildSpec(category, subcategory, search, brand, score, minPrice, maxPrice)
                .and(oneVariantPerProduct());
        return variantProductRepository.findAll(spec, pageable)
                .map(variantProductMapper::toListPublicResponseDTO);
    }

    /** Restringe el resultado a la variante representativa (menor id) de cada producto. */
    private Specification<VariantProduct> oneVariantPerProduct() {
        return (root, query, cb) -> {
            Subquery<Long> sub = query.subquery(Long.class);
            Root<VariantProduct> subRoot = sub.from(VariantProduct.class);
            sub.select(cb.least(subRoot.<Long>get("id")))
                    .where(
                            cb.equal(subRoot.get("isDeleted"), false),
                            cb.equal(subRoot.get("state"), true)
                    )
                    .groupBy(subRoot.get("product"));
            return root.get("id").in(sub);
        };
    }

    @Override
    public List<VariantProductListPublicResponseDTO> findVariantProductsByCategoryId(Long categoryId) {
        return variantProductRepository.findByCategoryId(categoryId, PageRequest.of(0, 10)).stream()
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
        return variantProductRepository.findBestSelling(PageRequest.of(0, 10)).stream()
                .map(variantProductMapper::toListPublicResponseDTO)
                .collect(toList());
    }

    @Override
    public List<VariantProductListPublicResponseDTO> findVariantProductsDiscountedByCategoryId(Long categoryId) {
        return variantProductRepository.findDiscountedByCategoryId(categoryId, PageRequest.of(0, 10)).stream()
                .map(variantProductMapper::toListPublicResponseDTO)
                .collect(toList());
    }

    // ─── ADMIN CRUD ───────────────────────────────────────────────────────────

    @Override
    @Transactional
    public VariantProductDetailAdminResponseDTO updateVariantProduct(
            Long id, VariantProductRequestDTO dto, CustomUserPrincipal principal) {
        dto.setSku(dto.getSku().trim().toLowerCase());
        VariantProduct variantProduct = variantProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Variant product with ID " + id + " not found"));
        variantProductMapper.updateEntityFromDTO(dto, variantProduct);
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

    // ─── HELPERS ──────────────────────────────────────────────────────────────

    private Pageable buildPageable(int page, int size, @Nullable String sortBy) {
        String valAsc = "Asc", valDesc = "Desc";
        String sortField = "id", sortDirection = valAsc;
        if (sortBy != null) {
            if (sortBy.contains(valDesc)) {
                sortField = sortBy.replace(valDesc, "");
                sortDirection = valDesc;
            } else if (sortBy.contains(valAsc)) {
                sortField = sortBy.replace(valAsc, "");
            }
        }
        Sort sort = sortDirection.equalsIgnoreCase(valDesc)
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();
        return PageRequest.of(page, size, sort);
    }

    private Specification<VariantProduct> buildSpec(
            @Nullable String category, @Nullable String subcategory,
            @Nullable String search, @Nullable String brand,
            @Nullable Double score, @Nullable Double minPrice, @Nullable Double maxPrice) {

        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            // Solo activos y no eliminados
            predicates.add(cb.equal(root.get("isDeleted"), false));
            predicates.add(cb.equal(root.get("state"), true));

            if (search != null && !search.isBlank()) {
                Join<VariantProduct, Product> pJoin = root.join("product");
                predicates.add(cb.or(
                        cb.like(cb.lower(pJoin.get("name")), "%" + search.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("sku")), "%" + search.toLowerCase() + "%")
                ));
            }
            if (score != null && score > 0) {
                Join<VariantProduct, Product> pJoin = root.join("product");
                predicates.add(cb.greaterThanOrEqualTo(pJoin.get("score"), score));
            }
            if (brand != null && !brand.isBlank()) {
                Join<VariantProduct, Product> pJoin = root.join("product");
                Join<Product, Brand> bJoin = pJoin.join("brand");
                predicates.add(cb.equal(cb.lower(bJoin.get("name")), brand.toLowerCase()));
            }
            if (subcategory != null && !subcategory.isBlank()) {
                Join<VariantProduct, Product> pJoin = root.join("product");
                Join<Product, Subcategory> sJoin = pJoin.join("subcategories");
                predicates.add(cb.equal(cb.lower(sJoin.get("name")), subcategory.toLowerCase()));
            } else if (category != null && !category.isBlank()) {
                // Solo filtra por categoría padre si no se pidió ya una subcategoría específica.
                Join<VariantProduct, Product> pJoin = root.join("product");
                Join<Product, Subcategory> sJoin = pJoin.join("subcategories");
                Join<Subcategory, Category> cJoin = sJoin.join("category");
                predicates.add(cb.equal(cb.lower(cJoin.get("name")), category.toLowerCase()));
            }
            if (maxPrice != null && maxPrice > 0)
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), BigDecimal.valueOf(maxPrice)));
            if (minPrice != null && minPrice > 0)
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), BigDecimal.valueOf(minPrice)));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
