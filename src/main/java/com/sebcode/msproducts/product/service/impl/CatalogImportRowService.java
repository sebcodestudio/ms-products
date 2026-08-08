package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.category.entity.Subcategory;
import com.sebcode.msproducts.category.repository.SubcategoryRepository;
import com.sebcode.msproducts.exception.BadRequestException;
import com.sebcode.msproducts.product.entity.*;
import com.sebcode.msproducts.product.enums.ImageType;
import com.sebcode.msproducts.product.repository.*;
import com.sebcode.msproducts.product.service.CatalogImportRow;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Procesa una fila del CSV de importación masiva en su propia transacción
 * (Propagation.REQUIRES_NEW) para que una fila con error no revierta las
 * demás filas ya procesadas del mismo archivo.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogImportRowService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final VariantProductRepository variantProductRepository;
    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final VariantAttributeRepository variantAttributeRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String importRow(CatalogImportRow row, CustomUserPrincipal principal) {
        Long uid = principal.getId();

        requireText(row.producto(), "producto");
        requireText(row.marca(), "marca");
        requireText(row.subcategoria(), "subcategoria");
        requireText(row.sku(), "sku");
        BigDecimal precio = parseDecimal(row.precio(), "precio");
        int stock = parseInt(row.stock(), "stock");

        Brand brand = brandRepository.findByName(row.marca().trim())
                .orElseGet(() -> brandRepository.save(Brand.builder()
                        .name(row.marca().trim())
                        .state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid)
                        .build()));

        Subcategory subcategory = subcategoryRepository.findByName(row.subcategoria().trim())
                .orElseThrow(() -> new BadRequestException(
                        "Subcategoria no encontrada: '" + row.subcategoria() + "' (debe existir de antemano)"));

        Product product = productRepository.findByNameAndBrandId(row.producto().trim(), brand.getId())
                .orElseGet(() -> productRepository.save(Product.builder()
                        .name(row.producto().trim())
                        .brand(brand)
                        .state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid)
                        .build()));

        if (!product.getSubcategories().contains(subcategory)) {
            product.addSubcategory(subcategory);
            product = productRepository.save(product);
        }

        String sku = row.sku().trim().toLowerCase();
        BigDecimal precioOriginal = StringUtils.hasText(row.precioOriginal())
                ? parseDecimal(row.precioOriginal(), "precioOriginal") : precio;
        int descuento = StringUtils.hasText(row.descuento()) ? parseInt(row.descuento(), "descuento") : 0;

        VariantProduct variant = variantProductRepository.findBySku(sku).orElse(null);
        if (variant == null) {
            variant = VariantProduct.builder()
                    .sku(sku)
                    .price(precio)
                    .originalPrice(precioOriginal)
                    .discount(descuento)
                    .stock(stock)
                    .soldCount(0)
                    .product(product)
                    .state(true).isDeleted(false)
                    .createdUser(uid).updateUser(uid)
                    .build();
        } else {
            variant.setPrice(precio);
            variant.setOriginalPrice(precioOriginal);
            variant.setDiscount(descuento);
            variant.setStock(stock);
            variant.setProduct(product);
            variant.setUpdateUser(uid);
        }
        variant = variantProductRepository.save(variant);

        AttributeValue colorValue = resolveAttributeValue("Color", row.color(), true, uid);
        upsertVariantAttribute(variant, colorValue, uid);

        AttributeValue tallaValue = resolveAttributeValue("Talla", row.talla(), false, uid);
        upsertVariantAttribute(variant, tallaValue, uid);

        addImageIfPresent(product, row.imagenUrl1(), colorValue, uid);
        addImageIfPresent(product, row.imagenUrl2(), colorValue, uid);

        return sku;
    }

    private AttributeValue resolveAttributeValue(String typeName, String rawValue, boolean isVisualType, Long uid) {
        if (!StringUtils.hasText(rawValue)) return null;
        String value = rawValue.trim();

        AttributeType type = attributeTypeRepository.findByName(typeName)
                .orElseGet(() -> attributeTypeRepository.save(AttributeType.builder()
                        .name(typeName)
                        .isVisual(isVisualType)
                        .state(true).isDeleted(false)
                        .build()));

        return attributeValueRepository.findByValueAndAttributeTypeId(value, type.getId())
                .orElseGet(() -> attributeValueRepository.save(AttributeValue.builder()
                        .value(value)
                        .attributeType(type)
                        .state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid)
                        .build()));
    }

    private void upsertVariantAttribute(VariantProduct variant, AttributeValue value, Long uid) {
        if (value == null) return;

        variantAttributeRepository.findByVariantProductIdAndAttributeTypeId(variant.getId(), value.getAttributeType().getId())
                .ifPresentOrElse(
                        existing -> {
                            existing.setAttributeValue(value);
                            existing.setUpdateUser(uid);
                            variantAttributeRepository.save(existing);
                        },
                        () -> variantAttributeRepository.save(VariantAttribute.builder()
                                .variantProduct(variant)
                                .attributeType(value.getAttributeType())
                                .attributeValue(value)
                                .state(true).isDeleted(false)
                                .createdUser(uid).updateUser(uid)
                                .build())
                );
    }

    private void addImageIfPresent(Product product, String imageUrl, AttributeValue colorValue, Long uid) {
        if (!StringUtils.hasText(imageUrl)) return;
        String url = imageUrl.trim();
        if (productImageRepository.findByImageUrl(url).isPresent()) return; // ya cargada, evita duplicados

        boolean isMain = !productImageRepository.existsByProductIdAndIsMainTrue(product.getId());
        productImageRepository.save(ProductImage.builder()
                .imageUrl(url)
                .imageOrder(0)
                .isMain(isMain)
                .imageType(ImageType.GALLERY)
                .attributeValue(colorValue)
                .product(product)
                .state(true).isDeleted(false)
                .createdUser(uid).updateUser(uid)
                .build());
    }

    private void requireText(String value, String field) {
        if (!StringUtils.hasText(value)) {
            throw new BadRequestException("La columna '" + field + "' es obligatoria");
        }
    }

    private BigDecimal parseDecimal(String raw, String field) {
        try {
            return new BigDecimal(raw.trim());
        } catch (Exception e) {
            throw new BadRequestException("Valor invalido en '" + field + "': '" + raw + "'");
        }
    }

    private int parseInt(String raw, String field) {
        try {
            return Integer.parseInt(raw.trim());
        } catch (Exception e) {
            throw new BadRequestException("Valor invalido en '" + field + "': '" + raw + "'");
        }
    }
}
