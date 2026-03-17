package com.sebcode.msproducts.category.service;

import com.sebcode.msproducts.category.dto.request.SubcategoryRequestDTO;
import com.sebcode.msproducts.category.dto.response.SubcategoryDetailResponseDTO;
import com.sebcode.msproducts.category.dto.response.SubcategoryListResponseDTO;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ISubcategoryService {

    SubcategoryDetailResponseDTO createSubcategory(SubcategoryRequestDTO subcategoryRequestDTO, CustomUserPrincipal principal);

    Optional<SubcategoryDetailResponseDTO> getSubcategoryById(Long id);

    List<SubcategoryListResponseDTO> getAllList(Long id);

    Page<SubcategoryListResponseDTO> searchAllSubcategories(String search, Boolean isVisual, int page, int size, String sortBy);

    SubcategoryDetailResponseDTO updateSubcategory(Long id, SubcategoryRequestDTO subcategoryRequestDTO, CustomUserPrincipal principal);

    void deleteSubcategory(Long id, CustomUserPrincipal principal);

}
