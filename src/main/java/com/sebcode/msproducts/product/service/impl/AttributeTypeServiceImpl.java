package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.AttributeTypeRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeTypeDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeTypeListResponseDTO;
import com.sebcode.msproducts.product.entity.AttributeType;
import com.sebcode.msproducts.product.mapper.AttributeTypeMapper;
import com.sebcode.msproducts.product.repository.AttributeTypeRepository;
import com.sebcode.msproducts.product.service.IAttributeTypeService;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttributeTypeServiceImpl implements IAttributeTypeService {

    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeTypeMapper attributeTypeMapper;

    @Override
    @Transactional
    public AttributeTypeDetailResponseDTO createAttributeType(AttributeTypeRequestDTO attributeTypeRequestDTO, CustomUserPrincipal principal) {
        attributeTypeRequestDTO.setName(attributeTypeRequestDTO.getName().trim().toLowerCase());
        Optional<AttributeType> existing = attributeTypeRepository.findByName(attributeTypeRequestDTO.getName());

        AttributeType attributeType;
        if (existing.isPresent() && existing.get().getIsDeleted()) {
            attributeType = existing.get();
            attributeType.setIsDeleted(false);
            attributeType.setDeleteAt(null);
            attributeTypeMapper.updateEntityFromDTO(attributeTypeRequestDTO, attributeType);
        } else if (existing.isPresent()) {
            throw new DuplicateRequestException("Attribute type with name '" + attributeTypeRequestDTO.getName() + "' already exists");
        } else {
            attributeType = attributeTypeMapper.toEntity(attributeTypeRequestDTO);
        }

        AttributeType saved = attributeTypeRepository.save(attributeType);
        log.info("Attribute type created: {} by admin: {}", attributeTypeRequestDTO.getName(), principal.getId());
        return attributeTypeMapper.toDetailResponseDTO(saved);
    }

    @Override
    public Optional<AttributeTypeDetailResponseDTO> getAttributeTypeById(Long id) {
        return attributeTypeRepository.findById(id).map(attributeTypeMapper::toDetailResponseDTO);
    }

    @Override
    public Page<AttributeTypeListResponseDTO> searchAllAttributeTypes(String search, Boolean isVisual, int page, int size, String sortBy) {
        log.debug("Executing attribute type search");

        String valAsc = "Asc";
        String valDesc = "Desc";
        String sortField = "";
        String sortDirection = "";

        if (sortBy != null) {
            if (sortBy.contains(valAsc)) {
                sortField = sortBy.replace(valAsc, "");
                sortDirection = valAsc;
            }
            if (sortBy.contains(valDesc)) {
                sortField = sortBy.replace(valDesc, "");
                sortDirection = valDesc;
            }
        }
        if (sortField.isEmpty()) {
            sortField = "id";
        }
        Sort sort = sortDirection.equalsIgnoreCase(valDesc) ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<AttributeType> spec = (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            // Buscar por nombre
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
            }
            if (isVisual != null) {
                predicates.add(
                        cb.equal(root.get("isVisual"), isVisual));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return attributeTypeRepository.findAll(spec, pageable).map(attributeTypeMapper::toListResponseDTO);
    }

    @Override
    @Transactional
    public AttributeTypeDetailResponseDTO updateAttributeType(Long id, AttributeTypeRequestDTO attributeTypeRequestDTO, CustomUserPrincipal principal) {
        attributeTypeRequestDTO.setName(attributeTypeRequestDTO.getName().trim().toLowerCase());
        AttributeType attributeType = attributeTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attribute type with ID " + id + " not found"));

        attributeTypeMapper.updateEntityFromDTO(attributeTypeRequestDTO, attributeType);

        AttributeType saved = attributeTypeRepository.save(attributeType);
        log.info("Attribute type updated: {} by admin: {}", attributeType.getName(), principal.getId());
        return attributeTypeMapper.toDetailResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteAttributeType(Long id, CustomUserPrincipal principal) {
        AttributeType attributeType = attributeTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attribute type with ID " + id + " not found"));

        attributeType.setIsDeleted(true);
        attributeType.setDeleteAt(LocalDate.now().atStartOfDay());
        attributeTypeRepository.save(attributeType);
        log.info("Attribute type soft deleted: {} by admin: {}", attributeType.getName(), principal.getId());
    }

}
