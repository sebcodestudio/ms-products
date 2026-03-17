package com.sebcode.msproducts.product.service;

import com.sebcode.msproducts.product.dto.request.ProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductListResponseDTO;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IProductService {


    ProductDetailResponseDTO createProduct(ProductRequestDTO productRequestDTO, CustomUserPrincipal principal);

    Optional<ProductDetailResponseDTO> getProductById(Long id);

    Page<ProductListResponseDTO> searchAllProducts(String search, Boolean isVisual, int page, int size, String sortBy);

    ProductDetailResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO, CustomUserPrincipal principal);

    void deleteProduct(Long id, CustomUserPrincipal principal);

}
