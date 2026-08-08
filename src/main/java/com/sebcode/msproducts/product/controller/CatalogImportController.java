package com.sebcode.msproducts.product.controller;

import com.sebcode.msproducts.product.dto.response.admin.CatalogImportResultDTO;
import com.sebcode.msproducts.product.service.CatalogImportService;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
@Tag(name = "Catalog Import", description = "Carga masiva de productos/variantes/imágenes vía CSV")
public class CatalogImportController {

    private final CatalogImportService catalogImportService;

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    @PreAuthorize("@catalogAccess.canManageProducts(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Importa catálogo desde CSV",
            description = "Columnas: producto,marca,subcategoria,sku,precio,precioOriginal,descuento,stock," +
                    "color,talla,imagenUrl1,imagenUrl2. Solo precioOriginal, descuento, color, talla, " +
                    "imagenUrl1 e imagenUrl2 son opcionales. La subcategoria debe existir de antemano; " +
                    "marca, producto, variante (por sku) y valores de Color/Talla se crean o actualizan " +
                    "automáticamente. Cada fila se procesa independiente: una fila con error no afecta al resto.")
    public ResponseEntity<CatalogImportResultDTO> importCsv(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        log.info("Import masivo de catalogo iniciado por user: {}", principal.getId());
        CatalogImportResultDTO result = catalogImportService.importCsv(file, principal);
        log.info("Import masivo completado: {} filas, {} ok, {} errores",
                result.getTotalRows(), result.getSuccessCount(), result.getErrorCount());

        return ResponseEntity.ok(result);
    }
}
