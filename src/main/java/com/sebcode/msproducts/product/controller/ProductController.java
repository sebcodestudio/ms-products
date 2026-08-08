package com.sebcode.msproducts.product.controller;

import com.sebcode.msproducts.common.response.PageResponse;
import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.ProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductListResponseDTO;
import com.sebcode.msproducts.product.service.IProductService;
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

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController {

    private final IProductService productService;

    @PostMapping
    @PreAuthorize("@catalogAccess.canManageProducts(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create new product", description = "Creates a new product. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "409", description = "Name already exists")
    })
    public ResponseEntity<ProductDetailResponseDTO> create(
            @Valid @RequestBody ProductRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Creating product with name: {} by user: {}", requestDTO.getName(), principal.getId());
        ProductDetailResponseDTO created = productService.createProduct(requestDTO, principal);
        log.info("Product created successfully with ID: {}", created.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves detailed information about a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductDetailResponseDTO> getById(
            @PathVariable @Positive(message = "ID must be positive") Long id) {

        log.debug("Retrieving product with ID: {}", id);

        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Product with ID: {} not found", id);
                    return new NotFoundException("Product with ID " + id + " not found");
                });
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search and filter products with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<PageResponse<ProductListResponseDTO>> search(
            @Parameter(description = "Search term for product name or description")
            @RequestParam(required = false) String search,

            @Parameter(description = "Filter by isVisual")
            @RequestParam(required = false) Boolean isVisual,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be at least 0")
            int page,

            @Parameter(description = "Number of items per page (max 100)")
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size must be at most 100")
            int size,

            @Parameter(description = "Sort by: idAsc, idDesc, nameAsc, nameDesc")
            @RequestParam(defaultValue = "idAsc") String sortBy) {

        log.info("Searching products - page: {}, size: {}, sortBy: {}", page, size, sortBy);

        Page<ProductListResponseDTO> products = productService.searchAllProducts(
                search, isVisual, page, size, sortBy);

        log.info("Search completed. Found {} attribute Type", products.getTotalElements());
        return ResponseEntity.ok(PageResponse.of(products));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageProducts(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update product", description = "Updates an existing product. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductDetailResponseDTO> update(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Updating product ID: {} by user: {}", id, principal.getId());
        ProductDetailResponseDTO updated = productService.updateProduct(id, requestDTO, principal);
        log.info("Product ID: {} updated successfully", id);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageProducts(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete product", description = "Soft deletes a product. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Deleting product ID: {} by user: {}", id, principal.getId());
        productService.deleteProduct(id, principal);
        log.info("Product ID: {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }

}