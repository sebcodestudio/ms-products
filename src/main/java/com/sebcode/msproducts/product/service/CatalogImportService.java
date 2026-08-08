package com.sebcode.msproducts.product.service;

import com.sebcode.msproducts.exception.BadRequestException;
import com.sebcode.msproducts.product.dto.response.admin.CatalogImportResultDTO;
import com.sebcode.msproducts.product.dto.response.admin.CatalogImportRowResultDTO;
import com.sebcode.msproducts.product.service.impl.CatalogImportRowService;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Carga masiva de catálogo (productos, variantes, atributos e imágenes) desde
 * un CSV. Cada fila se procesa en su propia transacción vía
 * CatalogImportRowService, así una fila con error no revierte el resto.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogImportService {

    private final CatalogImportRowService rowService;

    public CatalogImportResultDTO importCsv(MultipartFile file, CustomUserPrincipal principal) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("El archivo CSV esta vacio");
        }

        List<CatalogImportRowResultDTO> results = new ArrayList<>();
        int rowNumber = 1; // 1 = primera fila de datos (después del header)

        try (var reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreSurroundingSpaces(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            for (CSVRecord record : parser) {
                CatalogImportRow row = new CatalogImportRow(
                        get(record, "producto"),
                        get(record, "marca"),
                        get(record, "subcategoria"),
                        get(record, "sku"),
                        get(record, "precio"),
                        get(record, "precioOriginal"),
                        get(record, "descuento"),
                        get(record, "stock"),
                        get(record, "color"),
                        get(record, "talla"),
                        get(record, "imagenUrl1"),
                        get(record, "imagenUrl2")
                );
                try {
                    String sku = rowService.importRow(row, principal);
                    results.add(new CatalogImportRowResultDTO(rowNumber, true, "OK", sku));
                } catch (Exception e) {
                    log.warn("Fila {} del import fallo: {}", rowNumber, e.getMessage());
                    results.add(new CatalogImportRowResultDTO(rowNumber, false, e.getMessage(), row.sku()));
                }
                rowNumber++;
            }
        } catch (IOException e) {
            throw new BadRequestException("No se pudo leer el archivo CSV: " + e.getMessage());
        }

        long successCount = results.stream().filter(CatalogImportRowResultDTO::isSuccess).count();
        return new CatalogImportResultDTO(
                results.size(), (int) successCount, results.size() - (int) successCount, results);
    }

    private String get(CSVRecord record, String column) {
        return record.isMapped(column) ? record.get(column) : null;
    }
}
