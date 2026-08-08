package com.sebcode.msproducts.product.controller;

import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.ProductImageRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductImageDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductImageListResponseDTO;
import com.sebcode.msproducts.product.service.IProductImageService;
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
import com.sebcode.msproducts.common.response.PageResponse;
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
@RequestMapping("/api/v1/product-images")
@RequiredArgsConstructor
@Tag(name = "Product Images", description = "Endpoints for managing product images")
public class ProductImageController {

    private final IProductImageService productImageService;

    @PostMapping
    @PreAuthorize("@catalogAccess.canManageProducts(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create new product image", description = "Creates a new product image. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product image created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "409", description = "Name already exists")
    })
    public ResponseEntity<ProductImageDetailResponseDTO> create(
            @Valid @RequestBody ProductImageRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Creating product image with imageUrl: {} by user: {}", requestDTO.getImageUrl(), principal.getId());
        ProductImageDetailResponseDTO created = productImageService.createProductImage(requestDTO, principal);
        log.info("Product image created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product image by ID", description = "Retrieves detailed information about a specific product image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product image found"),
            @ApiResponse(responseCode = "404", description = "Product image not found")
    })
    public ResponseEntity<ProductImageDetailResponseDTO> getById(
            @PathVariable @Positive(message = "ID must be positive") Long id) {

        log.debug("Retrieving product image with ID: {}", id);

        return productImageService.getProductImageById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Product image with ID: {} not found", id);
                    return new NotFoundException("Product image with ID " + id + " not found");
                });
    }

    @GetMapping("/search")
    @Operation(summary = "Search product images", description = "Search and filter product images with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<PageResponse<ProductImageListResponseDTO>> search(
            @Parameter(description = "Search term for product image name or description")
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

            @Parameter(description = "Sort by: idAsc, idDesc, valueAsc, valueDesc")
            @RequestParam(defaultValue = "idAsc") String sortBy) {

        log.info("Searching product images - page: {}, size: {}, sortBy: {}", page, size, sortBy);

        Page<ProductImageListResponseDTO> productImages = productImageService.searchAllProductImages(
                search, isVisual, page, size, sortBy);

        log.info("Search completed. Found {} product image", productImages.getTotalElements());
        return ResponseEntity.ok(PageResponse.of(productImages));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageProducts(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update product image", description = "Updates an existing product image. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product image updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Product image not found")
    })
    public ResponseEntity<ProductImageDetailResponseDTO> update(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @Valid @RequestBody ProductImageRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Updating product image ID: {} by user: {}", id, principal.getId());
        ProductImageDetailResponseDTO updated = productImageService.updateProductImage(id, requestDTO, principal);
        log.info("Product image ID: {} updated successfully", id);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageProducts(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete product image", description = "Soft deletes a product image. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product image deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Product image not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Deleting product image ID: {} by user: {}", id, principal.getId());
        productImageService.deleteProductImage(id, principal);
        log.info("Product image ID: {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }

}