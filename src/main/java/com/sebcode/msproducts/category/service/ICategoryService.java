package com.sebcode.msproducts.category.service;

import com.sebcode.msproducts.category.dto.request.CategoryRequestDTO;
import com.sebcode.msproducts.category.dto.response.CategoryDetailResponseDTO;
import com.sebcode.msproducts.category.dto.response.CategoryListResponseDTO;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    CategoryDetailResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO, CustomUserPrincipal principal);

    Optional<CategoryDetailResponseDTO> getCategoryById(Long id);

    List<CategoryListResponseDTO> getAllList();

    Page<CategoryListResponseDTO> searchAllCategorys(String search, Boolean isVisual, int page, int size, String sortBy);

    CategoryDetailResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO, CustomUserPrincipal principal);

    void deleteCategory(Long id, CustomUserPrincipal principal);

}
