package com.proyecto.backendtiendavirtual.service;

// import com.proyecto.backendtiendavirtual.dto.response.ProductCardResponseDTO;
import com.proyecto.backendtiendavirtual.dto.response.ProductDetailResponseDTO;
import com.proyecto.backendtiendavirtual.dto.request.ProductRequestDTO;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;

// import java.util.List;
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
