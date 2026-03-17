package com.sebcode.msproducts.product.service;

import com.sebcode.msproducts.product.dto.request.AttributeTypeRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeTypeDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeTypeListResponseDTO;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IAttributeTypeService {

    AttributeTypeDetailResponseDTO createAttributeType(AttributeTypeRequestDTO attributeTypeRequestDTO, CustomUserPrincipal principal);

    Optional<AttributeTypeDetailResponseDTO> getAttributeTypeById(Long id);

    Page<AttributeTypeListResponseDTO> searchAllAttributeTypes(String search, Boolean isVisual, int page, int size, String sortBy);

    AttributeTypeDetailResponseDTO updateAttributeType(Long id, AttributeTypeRequestDTO attributeTypeRequestDTO, CustomUserPrincipal principal);

    void deleteAttributeType(Long id, CustomUserPrincipal principal);

}
