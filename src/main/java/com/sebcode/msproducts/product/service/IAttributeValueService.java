package com.sebcode.msproducts.product.service;

import com.sebcode.msproducts.product.dto.request.AttributeValueRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeTypeDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeValueDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeValueListResponseDTO;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IAttributeValueService {

    AttributeValueDetailResponseDTO createAttributeValue(AttributeValueRequestDTO attributeValueRequestDTO, CustomUserPrincipal principal);

    Optional<AttributeValueDetailResponseDTO> getAttributeValueById(Long id);

    Page<AttributeValueListResponseDTO> searchAllAttributeValues(String search, Boolean isVisual, int page, int size, String sortBy);

    AttributeValueDetailResponseDTO updateAttributeValue(Long id, AttributeValueRequestDTO attributeValueRequestDTO, CustomUserPrincipal principal);

    void deleteAttributeValue(Long id, CustomUserPrincipal principal);

}
