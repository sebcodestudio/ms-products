package com.sebcode.msproducts.company.controller;

import com.sebcode.msproducts.company.dto.request.CompanyRequestDTO;
import com.sebcode.msproducts.company.dto.response.CompanyResponseDTO;
import com.sebcode.msproducts.company.enums.CompanySort;
import com.sebcode.msproducts.company.service.ICompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

@RestController
@RequestMapping("/api/v1/admin/companies")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Company", description = "Company management APIs")
@SecurityRequirement(name = "bearerAuth")
public class CompanyController {

    private final ICompanyService companyService;

    @PostMapping
    @Operation(summary = "Create new Company (Admin only)")
    public ResponseEntity<CompanyResponseDTO> create(
            @Valid @RequestBody CompanyRequestDTO companyRequestDTO) {
        log.info("POST /api/v1/admin/companies - Creating company: {}", companyRequestDTO.getLegalName());
        CompanyResponseDTO response = companyService.createCompany(companyRequestDTO);
        log.debug("Company {} successfully created", companyRequestDTO.getLegalName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update company (Admin only)")
    public ResponseEntity<CompanyResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequestDTO companyRequestDTO) {
        log.info("PUT /api/v1/admin/companies/{} - Updating company", id);
        CompanyResponseDTO response = companyService.updateCompany(id, companyRequestDTO);
        log.debug("Company with ID {} successfully updated", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete company (Admin only)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Company deleted"),
                    @ApiResponse(responseCode = "404", description = "Company not found")
            })
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        log.info("DELETE /api/v1/admin/companies/{} - Deleting company", id);
        companyService.deleteCompany(id);
        log.debug("Company with ID {} successfully deleted", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company by id (Admin only)")
    public ResponseEntity<CompanyResponseDTO> getById(
            @PathVariable Long id) {
        log.info("GET /api/v1/admin/companies/{} - Retrieving company by ID", id);
        CompanyResponseDTO response = companyService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Search companies (Admin only)")
    public ResponseEntity<Page<CompanyResponseDTO>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ID_ASC") CompanySort sortBy) {
        log.info("GET /api/v1/admin/companies - Search: {}", search);
        Page<CompanyResponseDTO> companies = companyService.searchCompanies(search, active, page, size, sortBy);
        log.debug("Search result size: {}", companies.getTotalElements());
        return ResponseEntity.ok(companies);
    }

}