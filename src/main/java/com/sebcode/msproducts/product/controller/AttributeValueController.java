package com.sebcode.msproducts.product.controller;

import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.AttributeValueRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeValueDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeValueListResponseDTO;
import com.sebcode.msproducts.product.service.IAttributeValueService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/attribute-values")
@RequiredArgsConstructor
@Tag(name = "Attribute Values", description = "Endpoints for managing attribute values")
public class AttributeValueController {

    private final IAttributeValueService attributeValueService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create new attribute value", description = "Creates a new attribute value. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attribute value created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "409", description = "Name already exists")
    })
    public ResponseEntity<AttributeValueDetailResponseDTO> create(
            @Valid @RequestBody AttributeValueRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Creating attribute value with SKU: {} by user: {}", requestDTO.getValue(), principal.getId());
        AttributeValueDetailResponseDTO created = attributeValueService.createAttributeValue(requestDTO, principal);
        log.info("Attribute value created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attribute value by ID", description = "Retrieves detailed information about a specific attribute value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attribute value found"),
            @ApiResponse(responseCode = "404", description = "Attribute value not found")
    })
    public ResponseEntity<AttributeValueDetailResponseDTO> getById(
            @PathVariable @Positive(message = "ID must be positive") Long id) {

        log.debug("Retrieving attribute value with ID: {}", id);

        return attributeValueService.getAttributeValueById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Attribute value with ID: {} not found", id);
                    return new NotFoundException("Attribute value with ID " + id + " not found");
                });
    }

    @GetMapping("/search")
    @Operation(summary = "Search attribute values", description = "Search and filter attribute values with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<PageResponse<AttributeValueListResponseDTO>> search(
            @Parameter(description = "Search term for attribute value name or description")
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

        log.info("Searching attribute values - page: {}, size: {}, sortBy: {}", page, size, sortBy);

        Page<AttributeValueListResponseDTO> attributeValues = attributeValueService.searchAllAttributeValues(
                search, isVisual, page, size, sortBy);

        log.info("Search completed. Found {} attribute value", attributeValues.getTotalElements());
        return ResponseEntity.ok(PageResponse.of(attributeValues));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update attribute value", description = "Updates an existing attribute value. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attribute value updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Attribute value not found")
    })
    public ResponseEntity<AttributeValueDetailResponseDTO> update(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @Valid @RequestBody AttributeValueRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Updating attribute value ID: {} by user: {}", id, principal.getId());
        AttributeValueDetailResponseDTO updated = attributeValueService.updateAttributeValue(id, requestDTO, principal);
        log.info("Attribute value ID: {} updated successfully", id);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete attribute value", description = "Soft deletes a attribute value. Only accessible by administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attribute value deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Attribute value not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "ID must be positive") Long id,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Deleting attribute value ID: {} by user: {}", id, principal.getId());
        attributeValueService.deleteAttributeValue(id, principal);
        log.info("Attribute value ID: {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }

}