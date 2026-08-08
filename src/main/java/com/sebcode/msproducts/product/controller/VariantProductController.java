package com.sebcode.msproducts.product.controller;

import com.sebcode.msproducts.common.response.PageResponse;
import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.VariantProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductDetailAdminResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductListAdminResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductDetailPublicResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductListPublicResponseDTO;
import com.sebcode.msproducts.product.service.IVariantProductService;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/variant-products")
@RequiredArgsConstructor
@Tag(name = "Variant Products", description = "Endpoints for managing variant products")
public class VariantProductController {

    private final IVariantProductService variantProductService;

    @PostMapping
    @PreAuthorize("@catalogAccess.canManageVariants(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create new variant product", description = "Creates a new variant product. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Variant product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "409", description = "SKU already exists")
    })
    public ResponseEntity<VariantProductDetailAdminResponseDTO> create(
            @Valid @RequestBody VariantProductRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Creating variant product with SKU: {} by user: {}", requestDTO.getSku(), principal.getId());
        VariantProductDetailAdminResponseDTO created = variantProductService.createVariantProduct(requestDTO, principal);
        log.info("Variant product created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/admin/{id}")
    @Operation(summary = "Get variant product by ID", description = "Retrieves detailed information about a specific variant product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant product found"),
            @ApiResponse(responseCode = "404", description = "Variant product not found")
    })
    public ResponseEntity<VariantProductDetailAdminResponseDTO> getByIdAdmin(
            @PathVariable @Positive(message = "ID must be positive") Long id) {

        log.debug("Retrieving variant product admin with ID: {}", id);

        return variantProductService.getVariantProductsByIdAdmin(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Variant product admin with ID: {} not found", id);
                    return new NotFoundException("Variant product admin with ID " + id + " not found");
                });
    }

    @GetMapping("/admin/search")
    @Operation(summary = "Search variant products", description = "Search and filter variant products with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<PageResponse<VariantProductDetailAdminResponseDTO>> searchAdmin(
            @Parameter(description = "Search term for variant product name or description")
            @RequestParam(required = false) String search,

            @Parameter(description = "Filter by brand name")
            @RequestParam(required = false) String brand,

            @Parameter(description = "Minimum variant product score (0-5)")
            @RequestParam(required = false)
            @Min(value = 0, message = "Score must be at least 0")
            @Max(value = 5, message = "Score must be at most 5")
            Double score,

            @Parameter(description = "Filter by category name")
            @RequestParam(required = false) String category,

            @Parameter(description = "Filter by subcategory name")
            @RequestParam(required = false) String subcategory,

            @Parameter(description = "Minimum price filter")
            @RequestParam(required = false)
            @Min(value = 0, message = "Minimum price must be at least 0")
            Double minPrice,

            @Parameter(description = "Maximum price filter")
            @RequestParam(required = false)
            @Positive(message = "Maximum price must be positive")
            Double maxPrice,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be at least 0")
            int page,

            @Parameter(description = "Number of items per page (max 100)")
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size must be at most 100")
            int size,

            @Parameter(description = "Sort by: idAsc, idDesc, priceAsc, priceDesc, nameAsc, nameDesc")
            @RequestParam(defaultValue = "idAsc") String sortBy) {

        log.info("Searching variant products admin - page: {}, size: {}, sortBy: {}", page, size, sortBy);

        Page<VariantProductDetailAdminResponseDTO> variantProducts = variantProductService.searchAllVariantProductsAdmin(
                category, subcategory, brand, score, search, minPrice, maxPrice, page, size, sortBy);


        log.info("Search completed. Found {} variant products admin", variantProducts.getTotalElements());
        return ResponseEntity.ok(PageResponse.of(variantProducts));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get variant product by ID", description = "Retrieves detailed information about a specific variant product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant product found"),
            @ApiResponse(responseCode = "404", description = "Variant product not found")
    })
    public ResponseEntity<VariantProductDetailPublicResponseDTO> getByIdPublic(
            @PathVariable @Positive(message = "ID must be positive") Long id) {

        log.debug("Retrieving variant product public with ID: {}", id);

        return variantProductService.getVariantProductsByIdPublic(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Variant product public with ID: {} not found", id);
                    return new NotFoundException("Variant product public with ID " + id + " not found");
                });
    }

    @GetMapping("/search-public")
    @Operation(summary = "Search variant products", description = "Search and filter variant products with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<PageResponse<VariantProductListPublicResponseDTO>> searchPublic(
            @Parameter(description = "Search term for product name or description")
            @RequestParam(required = false) String search,

            @Parameter(description = "Filter by brand name")
            @RequestParam(required = false) String brand,

            @Parameter(description = "Minimum variant product score (0-5)")
            @RequestParam(required = false)
            @Min(value = 0, message = "Score must be at least 0")
            @Max(value = 5, message = "Score must be at most 5")
            Double score,

            @Parameter(description = "Filter by category name")
            @RequestParam(required = false) String category,

            @Parameter(description = "Filter by subcategory name")
            @RequestParam(required = false) String subcategory,

            @Parameter(description = "Minimum price filter")
            @RequestParam(required = false)
            @Min(value = 0, message = "Minimum price must be at least 0")
            Double minPrice,

            @Parameter(description = "Maximum price filter")
            @RequestParam(required = false)
            @Positive(message = "Maximum price must be positive")
            Double maxPrice,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be at least 0")
            int page,

            @Parameter(description = "Number of items per page (max 100)")
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size must be at most 100")
            int size,

            @Parameter(description = "Sort by: idAsc, idDesc, priceAsc, priceDesc, nameAsc, nameDesc")
            @RequestParam(defaultValue = "idAsc") String sortBy) {

        log.info("Searching variant products public - page: {}, size: {}, sortBy: {}", page, size, sortBy);

        Page<VariantProductListPublicResponseDTO> variantProducts = variantProductService.searchAllVariantProductsPublic(
                category, subcategory, brand, score, search, minPrice, maxPrice, page, size, sortBy);

        log.info("Search completed. Found {} variant products public", variantProducts.getTotalElements());
        return ResponseEntity.ok(PageResponse.of(variantProducts));
    }

    @GetMapping("/categories/{categoryId}")
    @Operation(summary = "Get variant products by category", description = "Retrieves all variant products belonging to a specific category")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    public ResponseEntity<List<VariantProductListPublicResponseDTO>> findByCategoryId(
            @PathVariable @Positive(message = "Category ID must be positive") Long categoryId) {

        log.info("Retrieving variant products for category ID: {}", categoryId);
        List<VariantProductListPublicResponseDTO> variantProducts = variantProductService.findVariantProductsByCategoryId(categoryId);
        log.info("Found {} variant products list for category ID: {}", variantProducts.size(), categoryId);

        return ResponseEntity.ok(variantProducts);
    }

    @GetMapping("/subcategories/{subcategoryId}")
    @Operation(summary = "Get variant products by subcategory", description = "Retrieves all variant products belonging to a specific subcategory")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    public ResponseEntity<List<VariantProductListPublicResponseDTO>> findBySubcategoryId(
            @PathVariable @Positive(message = "Subcategory ID must be positive") Long subcategoryId) {

        log.info("Retrieving variant products for subcategory ID: {}", subcategoryId);
        List<VariantProductListPublicResponseDTO> variantProducts = variantProductService.findVariantProductsBySubcategoryId(subcategoryId);
        log.info("Found {} variant products for subcategory ID: {}", variantProducts.size(), subcategoryId);

        return ResponseEntity.ok(variantProducts);
    }

    @GetMapping("/best-selling")
    @Operation(summary = "Get best selling variant products", description = "Retrieves the top selling variant products ordered by sales count")
    @ApiResponse(responseCode = "200", description = "Best selling variant products retrieved successfully")
    public ResponseEntity<List<VariantProductListPublicResponseDTO>> findBestSelling() {
        log.info("Retrieving best selling variant products");
        List<VariantProductListPublicResponseDTO> variantProducts = variantProductService.findBestSellingVariantProducts();
        log.info("Found {} best selling variant products", variantProducts.size());

        return ResponseEntity.ok(variantProducts);
    }

    @GetMapping("/categories/{categoryId}/discounted")
    @Operation(summary = "Get discounted variant products by category", description = "Retrieves variant products with active discounts from a specific category")
    @ApiResponse(responseCode = "200", description = "Discounted variant products retrieved successfully")
    public ResponseEntity<List<VariantProductListPublicResponseDTO>> findDiscountedByCategoryId(
            @PathVariable @Positive(message = "Category ID must be positive") Long categoryId) {

        log.info("Retrieving discounted variant products for category ID: {}", categoryId);
        List<VariantProductListPublicResponseDTO> variantProducts = variantProductService.findVariantProductsDiscountedByCategoryId(categoryId);
        log.info("Found {} discounted variant products for category ID: {}", variantProducts.size(), categoryId);

        return ResponseEntity.ok(variantProducts);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageVariants(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update variant product", description = "Updates an existing variant product. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Variant product not found")
    })
    public ResponseEntity<VariantProductDetailAdminResponseDTO> update(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @Valid @RequestBody VariantProductRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Updating variant product ID: {} by user: {}", id, principal.getId());
        VariantProductDetailAdminResponseDTO updated = variantProductService.updateVariantProduct(id, requestDTO, principal);
        log.info("Variant product ID: {} updated successfully", id);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageVariants(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete variant product", description = "Soft deletes a variant product. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Variant product deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Variant product not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Deleting variant product ID: {} by user: {}", id, principal.getId());
        variantProductService.deleteVariantProduct(id, principal);
        log.info("Variant product ID: {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }

}