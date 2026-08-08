package com.sebcode.msproducts.product.controller;

import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.AttributeTypeRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeTypeDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeTypeListResponseDTO;
import com.sebcode.msproducts.product.service.IAttributeTypeService;
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
@RequestMapping("/api/v1/attribute-types")
@RequiredArgsConstructor
@Tag(name = "Attribute Types", description = "Endpoints for managing attribute types")
public class AttributeTypeController {

    private final IAttributeTypeService attributeTypeService;

    @PostMapping
    @PreAuthorize("@catalogAccess.canManageTaxonomy(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create new attribute type", description = "Creates a new attribute type. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attribute type created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "409", description = "Name already exists")
    })
    public ResponseEntity<AttributeTypeDetailResponseDTO> create(
            @Valid @RequestBody AttributeTypeRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Creating attribute type with name: {} by user: {}", requestDTO.getName(), principal.getId());
        AttributeTypeDetailResponseDTO created = attributeTypeService.createAttributeType(requestDTO, principal);
        log.info("Attribute type created successfully with ID: {}", created.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get attribute type by ID", description = "Retrieves detailed information about a specific attribute type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attribute type found"),
            @ApiResponse(responseCode = "404", description = "Attribute type not found")
    })
    public ResponseEntity<AttributeTypeDetailResponseDTO> getById(
            @PathVariable @Positive(message = "ID must be positive") Long id) {

        log.debug("Retrieving attribute type with ID: {}", id);

        return attributeTypeService.getAttributeTypeById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Attribute type with ID: {} not found", id);
                    return new NotFoundException("Attribute type with ID " + id + " not found");
                });
    }

    @GetMapping("/search")
    @Operation(summary = "Search attribute types", description = "Search and filter attribute types with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<PageResponse<AttributeTypeListResponseDTO>> search(
            @Parameter(description = "Search term for attribute type name or description")
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

        log.info("Searching attribute types - page: {}, size: {}, sortBy: {}", page, size, sortBy);

        Page<AttributeTypeListResponseDTO> attributeTypes = attributeTypeService.searchAllAttributeTypes(
                search, isVisual, page, size, sortBy);

        log.info("Search completed. Found {} attribute Type", attributeTypes.getTotalElements());
        return ResponseEntity.ok(PageResponse.of(attributeTypes));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageTaxonomy(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update attribute type", description = "Updates an existing attribute type. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attribute type updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Attribute type not found")
    })
    public ResponseEntity<AttributeTypeDetailResponseDTO> update(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @Valid @RequestBody AttributeTypeRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Updating attribute type ID: {} by user: {}", id, principal.getId());
        AttributeTypeDetailResponseDTO updated = attributeTypeService.updateAttributeType(id, requestDTO, principal);
        log.info("Attribute type ID: {} updated successfully", id);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@catalogAccess.canManageTaxonomy(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete attribute type", description = "Soft deletes a attribute type. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attribute type deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Attribute type not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Deleting attribute type ID: {} by user: {}", id, principal.getId());
        attributeTypeService.deleteAttributeType(id, principal);
        log.info("Attribute type ID: {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }

}