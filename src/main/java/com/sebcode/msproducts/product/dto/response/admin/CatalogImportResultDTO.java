package com.sebcode.msproducts.product.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CatalogImportResultDTO {
    private int totalRows;
    private int successCount;
    private int errorCount;
    private List<CatalogImportRowResultDTO> rows;
}
