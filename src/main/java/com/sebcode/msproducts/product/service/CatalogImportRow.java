package com.sebcode.msproducts.product.service;

/**
 * Una fila del CSV de importación masiva de catálogo. Columnas esperadas:
 * producto,marca,subcategoria,sku,precio,precioOriginal,descuento,stock,color,talla,imagenUrl1,imagenUrl2
 * Las columnas precioOriginal, descuento, color, talla, imagenUrl1 e imagenUrl2 son opcionales.
 */
public record CatalogImportRow(
        String producto,
        String marca,
        String subcategoria,
        String sku,
        String precio,
        String precioOriginal,
        String descuento,
        String stock,
        String color,
        String talla,
        String imagenUrl1,
        String imagenUrl2
) {
}
