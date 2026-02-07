package com.soa.onlinestorebackend.product.service;

// import com.proyecto.backendtiendavirtual.dto.response.ProductCardResponseDTO;

import com.soa.onlinestorebackend.product.dto.request.ProductRequestDTO;
import com.soa.onlinestorebackend.product.dto.response.ProductDetailResponseDTO;

import java.util.Optional;

public interface IProductService {

    ProductDetailResponseDTO create(ProductRequestDTO productRequestDTO);

    ProductDetailResponseDTO update(Long id, ProductRequestDTO productRequestDTO);

    boolean delete(Long id);

    Optional<ProductDetailResponseDTO> getById(Long id);

    Optional<ProductDetailResponseDTO> getProductDetailById(Long id);

    // List<ProductCardResponseDTO> getAll();

    // Page<ProductCardResponseDTO> getAllCardFilter(String category, String subcategory, String brand, Double score,
            // String search, Double minPrice, Double maxPrice, Pageable pageable);

    // List<ProductCardResponseDTO> getBySold();

    // List<ProductCardResponseDTO> getByIdCategory(Long idCategory);

    // List<ProductCardResponseDTO> getDiscountByIdCategory(Long idCategory);

}
