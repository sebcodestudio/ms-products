package com.sebcode.msproducts.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    /**
     * Sube un archivo al bucket configurado bajo el prefijo/carpeta indicado
     * y devuelve la URL pública ya lista para usar en imageUrl.
     */
    String upload(MultipartFile file, String folder);
}
