package com.sebcode.msproducts.product.service;

import com.sebcode.msproducts.product.dto.request.VariantAttributeRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantAttributeDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantAttributeListResponseDTO;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IVariantAttributeService {

    VariantAttributeDetailResponseDTO createVariantAttribute(VariantAttributeRequestDTO variantAttributeRequestDTO, CustomUserPrincipal principal);

    Optional<VariantAttributeDetailResponseDTO> getVariantAttributeById(Long id);

    Page<VariantAttributeListResponseDTO> searchAllVariantAttributes(String search, Boolean isVisual, int page, int size, String sortBy);

    VariantAttributeDetailResponseDTO updateVariantAttribute(Long id, VariantAttributeRequestDTO variantAttributeRequestDTO, CustomUserPrincipal principal);

    void deleteVariantAttribute(Long id, CustomUserPrincipal principal);

}
