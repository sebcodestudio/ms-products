package com.soa.onlinestorebackend.product.controller;

import com.soa.onlinestorebackend.exception.NotFoundException;
import com.soa.onlinestorebackend.product.dto.request.VariantProductRequestDTO;
import com.soa.onlinestorebackend.product.dto.response.VariantProductCardResponseDTO;
import com.soa.onlinestorebackend.product.service.IVariantProductService;
import com.soa.onlinestorebackend.security.config.security.CustomUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/variant-products")
@RequiredArgsConstructor
@Tag(name = "VariantProducts", description = "Variant Product management APIs")
public class VariantProductController {

    private final IVariantProductService variantProductService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "berearAuth")
    @Operation(summary = "Create new Variant Product (Admin only)")
    public ResponseEntity<Void> create(@Valid @RequestBody VariantProductRequestDTO variantProductRequestDTO,
                                       CustomUserPrincipal principal) {
        log.info("POST /api/v1/admin/variant-products - Creating variant product: {}", variantProductRequestDTO.getSku());
        variantProductService.createVariantProduct(variantProductRequestDTO, principal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get variant products by id")
    public ResponseEntity<VariantProductCardResponseDTO> getById(@PathVariable Long id) {
        log.info("GET /api/v1/variant-products {} - Retrieving variant product by ID", id);
        return variantProductService.getVariantProductsById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Variant product with ID {} not found", id);
                    return new NotFoundException("Variant product with ID " + id + " not found");
                });
    }

    @GetMapping("/search")
    @Operation(summary = "Search variant products")
    public ResponseEntity<Page<VariantProductCardResponseDTO>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double score,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String subcategory,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idAsc") String sortBy) {
        log.info("GET /api/v1/variant-products/search");
        Page<VariantProductCardResponseDTO> products = variantProductService.searchAllVariantProducts(category, subcategory,
                brand, score, search, minPrice, maxPrice, page, size, sortBy);
        log.info("Search result size: {}", products.getTotalElements());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories/{categoryId}")
    @Operation(summary = "Get variant products by category id")
    public ResponseEntity<List<VariantProductCardResponseDTO>> findByCategoryId(@PathVariable Long categoryId) {
        log.info("GET /api/v1/variant-products/categories/{categoryId}");
        List<VariantProductCardResponseDTO> products = variantProductService.findVariantProductsByCategoryId(categoryId);
        log.info("Variant products by category id result size: {}", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/subcategories/{subcategoryId}")
    @Operation(summary = "Get variant products by subcategory id")
    public ResponseEntity<List<VariantProductCardResponseDTO>> findBySubcategoryId(@PathVariable Long subcategoryId) {
        log.info("GET /api/v1/variant-products/subcategories/{subcategoryId}");
        List<VariantProductCardResponseDTO> products = variantProductService.findVariantProductsBySubcategoryId(subcategoryId);
        log.info("Variant products by subcategory id result size: {}", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/best-selling")
    @Operation(summary = "Get variant products by sold id")
    public ResponseEntity<List<VariantProductCardResponseDTO>> findBestSelling() {
        log.info("GET /api/v1/variant-products/best-selling");
        List<VariantProductCardResponseDTO> products = variantProductService.findBestSellingVariantProducts();
        log.info("Variant products best-selling result size: {}", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories/{categoryId}/discounted")
    @Operation(summary = "Get variant products discounted by category id")
    public ResponseEntity<List<VariantProductCardResponseDTO>> findDiscountedByCategoryId(@PathVariable Long categoryId) {
        log.info("GET /api/v1/variant-products/categories/{categoryId}/discounted");
        List<VariantProductCardResponseDTO> products = variantProductService.findVariantProductsDiscountedByCategoryId(categoryId);
        log.info("Variant products discounted by category id result size: {}", products.size());
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "berearAuth")
    @Operation(summary = "Update product (Seller only)")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody VariantProductRequestDTO variantProductRequestDTO,
                                       CustomUserPrincipal principal) {
        log.info("PUT /api/v1/admin/variant-products/{} - Updating product", id);
        variantProductService.updateVariantProduct(id, variantProductRequestDTO, principal);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       CustomUserPrincipal principal) {
        log.info("DELETE /api/v1/admin/variant-products/{} - Deleting product", id);
        variantProductService.deleteVariantProduct(id, principal);
        log.info("Variant product with ID {} successfully deleted", id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping()
//    @Operation(summary = "Get all variant products")
//    public ResponseEntity<List<VariantProductCardResponseDTO>> findAll() {
//        log.info("GET /api/v1/variant-products");
//        List<VariantProductCardResponseDTO> products = variantProductService.findAllVariantProducts();
//        log.info("Variant products result size: {}", products.size());
//        return ResponseEntity.ok(products);
//    }

//    @GetMapping("/categories/{categoryId}")
//    @Operation(summary = "Get variant products by category id")
//    public ResponseEntity<List<VariantProductCardResponseDTO>> findByCategoryId(@PathVariable Long categoryId) {
//        log.info("GET /api/v1/variant-products/categories/{categoryId}");
//        List<VariantProductCardResponseDTO> products = variantProductService.findVariantProductsByCategoryId(categoryId);
//        log.info("Variant products by category id result size: {}", products.size());
//        return ResponseEntity.ok(products);
//    }
//
//    @GetMapping("/subcategories/{subcategoryId}")
//    @Operation(summary = "Get variant products by subcategory id")
//    public ResponseEntity<List<VariantProductCardResponseDTO>> findBySubcategoryId(@PathVariable Long subcategoryId) {
//        log.info("GET /api/v1/variant-products/subcategories/{subcategoryId}");
//        List<VariantProductCardResponseDTO> products = variantProductService.findVariantProductsBySubcategoryId(subcategoryId);
//        log.info("Variant products by subcategory id result size: {}", products.size());
//        return ResponseEntity.ok(products);
//    }
//
//    @GetMapping("/best-selling")
//    @Operation(summary = "Get variant products by sold id")
//    public ResponseEntity<List<VariantProductCardResponseDTO>> findBestSelling() {
//        log.info("GET /api/v1/variant-products/best-selling");
//        List<VariantProductCardResponseDTO> products = variantProductService.findBestSellingVariantProducts();
//        log.info("Variant products best-selling result size: {}", products.size());
//        return ResponseEntity.ok(products);
//    }
//
//    @GetMapping("/categories/{categoryId}/discounted")
//    @Operation(summary = "Get variant products discounted by category id")
//    public ResponseEntity<List<VariantProductCardResponseDTO>> findDiscountedByCategoryId(@PathVariable Long categoryId) {
//        log.info("GET /api/v1/variant-products/categories/{categoryId}/discounted");
//        List<VariantProductCardResponseDTO> products = variantProductService.findVariantProductsDiscountedByCategoryId(categoryId);
//        log.info("Variant products discounted by category id result size: {}", products.size());
//        return ResponseEntity.ok(products);
//    }

}
