package com.sebcode.msproducts.product.controller;

import com.sebcode.msproducts.common.response.PageResponse;
import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.BrandRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.BrandDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.BrandListResponseDTO;
import com.sebcode.msproducts.product.service.IBrandService;
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
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
@Tag(name = "Brands", description = "Endpoints for managing brands")
public class BrandController {

    private final IBrandService brandService;

    @PostMapping
    @PreAuthorize("@catalogAccess.canManageTaxonomy(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create new brand", description = "Creates a new brand. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Brand created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "409", description = "Name already exists")
    })
    public ResponseEntity<BrandDetailResponseDTO> create(
            @Valid @RequestBody BrandRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Creating brand with name: {} by user: {}", requestDTO.getName(), principal.getId());
        BrandDetailResponseDTO created = brandService.createBrand(requestDTO, principal);
        log.info("Brand created successfully with ID: {}", created.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get brand by ID", description = "Retrieves detailed information about a specific brand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand found"),
            @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<BrandDetailResponseDTO> getById(
            @PathVariable @Positive(message = "ID must be positive") Long id) {

        log.debug("Retrieving brand with ID: {}", id);

        return brandService.getBrandById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Brand with ID: {} not found", id);
                    return new NotFoundException("Brand with ID " + id + " not found");
                });
    }

    @GetMapping("/search")
    @Operation(summary = "Search brands", description = "Search and filter brands with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<PageResponse<BrandListResponseDTO>> search(
            @Parameter(description = "Search term for brand name or description")
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

        log.info("Searching brands - page: {}, size: {}, sortBy: {}", page, size, sortBy);

        Page<BrandListResponseDTO> brands = brandService.searchAllBrands(
                search, isVisual, page, size, sortBy);

        log.info("Search completed. Found {} attribute Type", brands.getTotalElements());
        return ResponseEntity.ok(PageResponse.of(brands));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageTaxonomy(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update brand", description = "Updates an existing brand. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<BrandDetailResponseDTO> update(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @Valid @RequestBody BrandRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Updating brand ID: {} by user: {}", id, principal.getId());
        BrandDetailResponseDTO updated = brandService.updateBrand(id, requestDTO, principal);
        log.info("Brand ID: {} updated successfully", id);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageTaxonomy(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete brand", description = "Soft deletes a brand. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Brand deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Deleting brand ID: {} by user: {}", id, principal.getId());
        brandService.deleteBrand(id, principal);
        log.info("Brand ID: {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }

}