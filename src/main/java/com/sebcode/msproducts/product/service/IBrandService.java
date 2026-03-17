package com.sebcode.msproducts.product.service;

import com.sebcode.msproducts.product.dto.request.BrandRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.BrandDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.BrandListResponseDTO;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IBrandService {

    BrandDetailResponseDTO createBrand(BrandRequestDTO brandRequestDTO, CustomUserPrincipal principal);

    Optional<BrandDetailResponseDTO> getBrandById(Long id);

    Page<BrandListResponseDTO> searchAllBrands(String search, Boolean isVisual, int page, int size, String sortBy);

    BrandDetailResponseDTO updateBrand(Long id, BrandRequestDTO brandRequestDTO, CustomUserPrincipal principal);

    void deleteBrand(Long id, CustomUserPrincipal principal);

}
