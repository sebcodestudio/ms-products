package com.sebcode.msproducts.storage.service.impl;

import com.sebcode.msproducts.exception.BadRequestException;
import com.sebcode.msproducts.exception.StorageException;
import com.sebcode.msproducts.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3StorageServiceImpl implements StorageService {

    // Carpetas válidas dentro del bucket — evita que un folder arbitrario del
    // request escriba fuera de la estructura esperada por el frontend
    // (categories/, subcategories/, products/, variant-products/).
    private static final Set<String> ALLOWED_FOLDERS = Set.of(
            "categories", "subcategories", "brands", "products", "variant-products");

    private static final Map<String, String> EXTENSION_BY_CONTENT_TYPE = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp",
            "image/gif", ".gif"
    );

    private final S3Client s3Client;

    @Value("${app.storage.bucket}")
    private String bucket;

    @Value("${app.storage.public-base-url}")
    private String publicBaseUrl;

    @Override
    public String upload(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("El archivo esta vacio");
        }
        if (!ALLOWED_FOLDERS.contains(folder)) {
            throw new BadRequestException(
                    "Carpeta invalida. Debe ser una de: " + String.join(", ", ALLOWED_FOLDERS));
        }
        String extension = EXTENSION_BY_CONTENT_TYPE.get(
                file.getContentType() == null ? "" : file.getContentType().toLowerCase());
        if (extension == null) {
            throw new BadRequestException(
                    "Tipo de imagen no soportado: " + file.getContentType() + ". Usa JPEG, PNG, WEBP o GIF.");
        }

        String key = folder + "/" + UUID.randomUUID() + extension;
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .contentLength(file.getSize())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new BadRequestException("No se pudo leer el archivo: " + e.getMessage());
        } catch (SdkException e) {
            // Cubre tanto errores de conectividad (SdkClientException — no se
            // pudo alcanzar el endpoint, ej. credenciales/endpoint mal
            // configurados) como errores de respuesta del servicio S3/R2.
            log.error("Fallo subiendo '{}' al bucket '{}'", key, bucket, e);
            throw new StorageException("No se pudo subir la imagen al almacenamiento", e);
        }

        String url = publicBaseUrl.replaceAll("/+$", "") + "/" + key;
        log.info("Imagen subida: {}", url);
        return url;
    }
}
