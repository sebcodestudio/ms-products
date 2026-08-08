package com.sebcode.msproducts.product.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CatalogImportRowResultDTO {
    private int row;
    private boolean success;
    private String message;
    private String sku;
}
