package com.sebcode.msproducts.product.service;

import com.sebcode.msproducts.product.dto.request.VariantProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductDetailAdminResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductDetailPublicResponseDTO;
import com.sebcode.msproducts.product.dto.response.customer.VariantProductListPublicResponseDTO;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IVariantProductService {

    VariantProductDetailAdminResponseDTO createVariantProduct(VariantProductRequestDTO variantProductRequestDTO, CustomUserPrincipal principal);

    Optional<VariantProductDetailAdminResponseDTO> getVariantProductsByIdAdmin(Long id);

    Page<VariantProductDetailAdminResponseDTO> searchAllVariantProductsAdmin(String category, String subcategory, String brand, Double score,
                                                                             String search, Double minPrice, Double maxPrice, int page, int size, String sortBy);

    Optional<VariantProductDetailPublicResponseDTO> getVariantProductsByIdPublic(Long id);

    Page<VariantProductListPublicResponseDTO> searchAllVariantProductsPublic(String category, String subcategory, String brand, Double score,
                                                                             String search, Double minPrice, Double maxPrice, int page, int size, String sortBy);

    List<VariantProductListPublicResponseDTO> findVariantProductsByCategoryId(Long categoryId);

    List<VariantProductListPublicResponseDTO> findVariantProductsBySubcategoryId(Long subcategoryId);

    List<VariantProductListPublicResponseDTO> findBestSellingVariantProducts();

    List<VariantProductListPublicResponseDTO> findVariantProductsDiscountedByCategoryId(Long categoryId);

    VariantProductDetailAdminResponseDTO updateVariantProduct(Long id, VariantProductRequestDTO variantProductRequestDTO, CustomUserPrincipal principal);

    void deleteVariantProduct(Long id, CustomUserPrincipal principal);

}
