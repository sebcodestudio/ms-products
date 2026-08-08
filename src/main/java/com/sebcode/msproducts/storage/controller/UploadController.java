package com.sebcode.msproducts.storage.controller;

import com.sebcode.msproducts.storage.dto.UploadResponseDTO;
import com.sebcode.msproducts.storage.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
@Tag(name = "Uploads", description = "Sube imágenes al almacenamiento (R2/MinIO) y devuelve su URL pública")
public class UploadController {

    private final StorageService storageService;

    @PostMapping(value = "/images", consumes = "multipart/form-data")
    @PreAuthorize("@catalogAccess.canManageProducts(authentication)")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Sube una imagen",
            description = "Sube un archivo de imagen (JPEG/PNG/WEBP/GIF, máx 10MB) a la carpeta indicada " +
                    "y devuelve la URL pública lista para usar como imageUrl en categorías, marcas, " +
                    "productos o imágenes de producto.")
    public ResponseEntity<UploadResponseDTO> uploadImage(
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "categories | subcategories | brands | products | variant-products")
            @RequestParam("folder") String folder) {

        String url = storageService.upload(file, folder);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UploadResponseDTO(url));
    }
}
