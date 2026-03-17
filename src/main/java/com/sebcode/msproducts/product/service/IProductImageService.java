package com.sebcode.msproducts.product.service;

import com.sebcode.msproducts.product.dto.request.ProductImageRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductImageDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductImageListResponseDTO;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IProductImageService {

    ProductImageDetailResponseDTO createProductImage(ProductImageRequestDTO productImageRequestDTO, CustomUserPrincipal principal);

    Optional<ProductImageDetailResponseDTO> getProductImageById(Long id);

    Page<ProductImageListResponseDTO> searchAllProductImages(String search, Boolean isVisual, int page, int size, String sortBy);

    ProductImageDetailResponseDTO updateProductImage(Long id, ProductImageRequestDTO productImageRequestDTO, CustomUserPrincipal principal);

    void deleteProductImage(Long id, CustomUserPrincipal principal);

}
