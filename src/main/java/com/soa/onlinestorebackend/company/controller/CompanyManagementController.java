package com.soa.onlinestorebackend.company.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/companies/{companyId}")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Company", description = "Company management APIs")
@SecurityRequirement(name = "bearerAuth")
public class CompanyManagementController {

//    private final ICompanyService companyService;
//
//    // ✅ OWNER o ADMIN de la compañía pueden editar SU compañía
//    @PutMapping
//    public ResponseEntity<?> updateCompany(
//            @PathVariable Long companyId,
//            @RequestBody CompanyRequestDTO dto,
//            @AuthenticationPrincipal CustomUserPrincipal principal) {
//
//        // Validar que el usuario pertenezca a esa compañía
//        if (!principal.belongsToCompany(companyId)) {
//            throw new ForbiddenException("You don't belong to this company");
//        }
//
//        // Validar que tenga rol de OWNER o ADMIN
//        if (!principal.hasCompanyRole(CompanyRole.OWNER) &&
//                !principal.hasCompanyRole(CompanyRole.ADMIN)) {
//            throw new ForbiddenException("You don't have permission to edit this company");
//        }
//
//        companyService.updateCompany(companyId, dto);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/{id}")
//    @Operation(summary = "Update company (Admin only)")
//    public ResponseEntity<CompanyResponseDTO> update(
//            @PathVariable Long id,
//            @Valid @RequestBody CompanyRequestDTO companyRequestDTO) {
//        log.info("PUT /api/v1/admin/companies/{} - Updating company", id);
//        CompanyResponseDTO response = companyService.updateCompany(id, companyRequestDTO);
//        log.debug("Company with ID {} successfully updated", id);
//        return ResponseEntity.ok(response);
//    }
}
