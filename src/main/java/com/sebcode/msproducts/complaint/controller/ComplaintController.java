package com.sebcode.msproducts.complaint.controller;

import com.sebcode.msproducts.complaint.dto.request.ComplaintRequestDTO;
import com.sebcode.msproducts.complaint.dto.response.ComplaintListResponseDTO;
import com.sebcode.msproducts.complaint.dto.response.ComplaintResponseDTO;
import com.sebcode.msproducts.complaint.service.IComplaintService;
import com.sebcode.msproducts.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
@Tag(name = "Complaints", description = "Libro de Reclamaciones — registro y consulta de reclamos/quejas")
public class ComplaintController {

    private final IComplaintService complaintService;

    @PostMapping
    @Operation(summary = "Registrar un reclamo o queja", description = "Endpoint público del Libro de Reclamaciones virtual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reclamo/queja registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ComplaintResponseDTO> create(@Valid @RequestBody ComplaintRequestDTO requestDTO) {
        log.info("Nuevo reclamo/queja recibido (tipo: {})", requestDTO.getType());
        ComplaintResponseDTO created = complaintService.createComplaint(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar reclamos/quejas", description = "Solo accesible por administradores")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    public ResponseEntity<PageResponse<ComplaintListResponseDTO>> search(
            @Parameter(description = "Número de página (0-based)")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be at least 0")
            int page,

            @Parameter(description = "Tamaño de página (máx 100)")
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size must be at most 100")
            int size) {

        Page<ComplaintListResponseDTO> complaints = complaintService.searchComplaints(page, size);
        return ResponseEntity.ok(PageResponse.of(complaints));
    }

}
