package com.soa.onlinestorebackend.product.service;

import com.soa.onlinestorebackend.product.dto.request.VariantProductRequestDTO;
import com.soa.onlinestorebackend.product.dto.response.VariantProductCardResponseDTO;
import com.soa.onlinestorebackend.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IVariantProductService {

    void createVariantProduct(VariantProductRequestDTO variantProductRequestDTO, CustomUserPrincipal principal);

    Optional<VariantProductCardResponseDTO> getVariantProductsById(Long id);

    Page<VariantProductCardResponseDTO> searchAllVariantProducts(String category, String subcategory, String brand, Double score,
                                                                 String search, Double minPrice, Double maxPrice, int page, int size, String sortBy);

    List<VariantProductCardResponseDTO> findVariantProductsByCategoryId(Long categoryId);

    List<VariantProductCardResponseDTO> findVariantProductsBySubcategoryId(Long subcategoryId);

    List<VariantProductCardResponseDTO> findBestSellingVariantProducts();

    List<VariantProductCardResponseDTO> findVariantProductsDiscountedByCategoryId(Long categoryId);

    void updateVariantProduct(Long id, VariantProductRequestDTO variantProductRequestDTO, CustomUserPrincipal principal);

    void deleteVariantProduct(Long id, CustomUserPrincipal principal);

}
