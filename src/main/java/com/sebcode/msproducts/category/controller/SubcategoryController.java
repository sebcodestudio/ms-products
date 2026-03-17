package com.sebcode.msproducts.category.controller;

import com.sebcode.msproducts.category.dto.request.SubcategoryRequestDTO;
import com.sebcode.msproducts.category.dto.response.SubcategoryDetailResponseDTO;
import com.sebcode.msproducts.category.dto.response.SubcategoryListResponseDTO;
import com.sebcode.msproducts.category.service.ISubcategoryService;
import com.sebcode.msproducts.common.entity.AuditableEntity;
import com.sebcode.msproducts.exception.NotFoundException;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/subcategories")
@RequiredArgsConstructor
@Tag(name = "Subcategories", description = "Endpoints for managing subcategories")
public class SubcategoryController extends AuditableEntity {

    private final ISubcategoryService subcategoryService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create new subcategory", description = "Creates a new subcategory. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subcategory created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "409", description = "Name already exists")
    })
    public ResponseEntity<SubcategoryDetailResponseDTO> create(
            @Valid @RequestBody SubcategoryRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Creating subcategory with SKU: {} by user: {}", requestDTO.getName(), principal.getId());
        SubcategoryDetailResponseDTO created = subcategoryService.createSubcategory(requestDTO, principal);
        log.info("Subcategory created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subcategory by ID", description = "Retrieves detailed information about a specific subcategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subcategory found"),
            @ApiResponse(responseCode = "404", description = "Subcategory not found")
    })
    public ResponseEntity<SubcategoryDetailResponseDTO> getById(
            @PathVariable @Positive(message = "ID must be positive") Long id) {

        log.debug("Retrieving subcategory with ID: {}", id);

        return subcategoryService.getSubcategoryById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Subcategory with ID: {} not found", id);
                    return new NotFoundException("Subcategory with ID " + id + " not found");
                });
    }

    @GetMapping("/card/{id}")
    public ResponseEntity<List<SubcategoryListResponseDTO>> getAllList(@PathVariable Long id) {
        List<SubcategoryListResponseDTO> subcategories = subcategoryService.getAllList(id);
        return ResponseEntity.ok(subcategories);
    }

    @GetMapping("/search")
    @Operation(summary = "Search subcategorys", description = "Search and filter subcategorys with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<Page<SubcategoryListResponseDTO>> search(
            @Parameter(description = "Search term for subcategory name or description")
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

        log.info("Searching subcategorys - page: {}, size: {}, sortBy: {}", page, size, sortBy);

        Page<SubcategoryListResponseDTO> subcategorys = subcategoryService.searchAllSubcategories(
                search, isVisual, page, size, sortBy);

        log.info("Search completed. Found {} subcategory", subcategorys.getTotalElements());
        return ResponseEntity.ok(subcategorys);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update subcategory", description = "Updates an existing subcategory. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subcategory updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Subcategory not found")
    })
    public ResponseEntity<SubcategoryDetailResponseDTO> update(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @Valid @RequestBody SubcategoryRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Updating subcategory ID: {} by user: {}", id, principal.getId());
        SubcategoryDetailResponseDTO updated = subcategoryService.updateSubcategory(id, requestDTO, principal);
        log.info("Subcategory ID: {} updated successfully", id);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete subcategory", description = "Soft deletes a subcategory. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subcategory deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Subcategory not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Deleting subcategory ID: {} by user: {}", id, principal.getId());
        subcategoryService.deleteSubcategory(id, principal);
        log.info("Subcategory ID: {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }

}
