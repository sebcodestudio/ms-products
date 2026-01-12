package com.proyecto.backendtiendavirtual.service;

// import com.proyecto.backendtiendavirtual.dto.response.ProductCardResponseDTO;
// import com.proyecto.backendtiendavirtual.dto.response.ProductDetailResponseDTO;
// import com.proyecto.backendtiendavirtual.dto.request.ProductRequestDTO;
import com.proyecto.backendtiendavirtual.dto.response.VariantProductCardResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IVariantProductService {

    // ProductDetailResponseDTO create(ProductRequestDTO productRequestDTO);

    // ProductDetailResponseDTO update(Long id, ProductRequestDTO productRequestDTO);

    // boolean delete(Long id);

    Optional<VariantProductCardResponseDTO> getById(Long id);

    Optional<VariantProductCardResponseDTO> getProductDetailById(Long id);

    List<VariantProductCardResponseDTO> getAll();

    Page<VariantProductCardResponseDTO> getAllCardFilter(String category, String subcategory, String brand, Double score,
            String search, Double minPrice, Double maxPrice, Pageable pageable);

    List<VariantProductCardResponseDTO> getBySold();

    List<VariantProductCardResponseDTO> getByIdCategory(Long idCategory);

    List<VariantProductCardResponseDTO> getDiscountByIdCategory(Long idCategory);

}
