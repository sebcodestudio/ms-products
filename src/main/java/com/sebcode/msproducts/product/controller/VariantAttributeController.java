package com.sebcode.msproducts.product.controller;

import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.VariantAttributeRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantAttributeDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantAttributeListResponseDTO;
import com.sebcode.msproducts.product.service.IVariantAttributeService;
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
@RequestMapping("/api/v1/variant-attributes")
@RequiredArgsConstructor
@Tag(name = "Variant Attributes", description = "Endpoints for managing variant attributes")
public class VariantAttributeController {

    private final IVariantAttributeService variantAttributeService;

    @PostMapping
    @PreAuthorize("@catalogAccess.canManageVariants(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create new variant attribute", description = "Creates a new variant attribute. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Variant attribute created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "409", description = "Name already exists")
    })
    public ResponseEntity<VariantAttributeDetailResponseDTO> create(
            @Valid @RequestBody VariantAttributeRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Creating variant attribute with SKU: by user: {}", principal.getId());
        VariantAttributeDetailResponseDTO created = variantAttributeService.createVariantAttribute(requestDTO, principal);
        log.info("Variant attribute created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get variant attribute by ID", description = "Retrieves detailed information about a specific variant attribute")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant attribute found"),
            @ApiResponse(responseCode = "404", description = "Variant attribute not found")
    })
    public ResponseEntity<VariantAttributeDetailResponseDTO> getById(
            @PathVariable @Positive(message = "ID must be positive") Long id) {

        log.debug("Retrieving variant attribute with ID: {}", id);

        return variantAttributeService.getVariantAttributeById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Variant attribute with ID: {} not found", id);
                    return new NotFoundException("Variant attribute with ID " + id + " not found");
                });
    }

    @GetMapping("/search")
    @Operation(summary = "Search variant attributes", description = "Search and filter variant attributes with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<PageResponse<VariantAttributeListResponseDTO>> search(
            @Parameter(description = "Search term for variant attribute name or description")
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

        log.info("Searching variant attributes - page: {}, size: {}, sortBy: {}", page, size, sortBy);

        Page<VariantAttributeListResponseDTO> variantAttributes = variantAttributeService.searchAllVariantAttributes(
                search, isVisual, page, size, sortBy);

        log.info("Search completed. Found {} attribute Value", variantAttributes.getTotalElements());
        return ResponseEntity.ok(PageResponse.of(variantAttributes));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageVariants(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update variant attribute", description = "Updates an existing variant attribute. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant attribute updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Variant attribute not found")
    })
    public ResponseEntity<VariantAttributeDetailResponseDTO> update(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @Valid @RequestBody VariantAttributeRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Updating variant attribute ID: {} by user: {}", id, principal.getId());
        VariantAttributeDetailResponseDTO updated = variantAttributeService.updateVariantAttribute(id, requestDTO, principal);
        log.info("Variant attribute ID: {} updated successfully", id);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageVariants(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete variant attribute", description = "Soft deletes a variant attribute. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Variant attribute deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Variant attribute not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Deleting variant attribute ID: {} by user: {}", id, principal.getId());
        variantAttributeService.deleteVariantAttribute(id, principal);
        log.info("Variant attribute ID: {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }

}