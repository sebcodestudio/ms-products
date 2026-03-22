package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.exception.DuplicateResourceException;
import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.AttributeValueRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeValueDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.AttributeValueListResponseDTO;
import com.sebcode.msproducts.product.entity.AttributeValue;
import com.sebcode.msproducts.product.mapper.AttributeValueMapper;
import com.sebcode.msproducts.product.repository.AttributeTypeRepository;
import com.sebcode.msproducts.product.repository.AttributeValueRepository;
import com.sebcode.msproducts.product.service.IAttributeValueService;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
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
public class AttributeValueServiceImpl implements IAttributeValueService {

    private final AttributeValueRepository attributeValueRepository;
    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeValueMapper attributeValueMapper;

    @Override
    @Transactional
    public AttributeValueDetailResponseDTO createAttributeValue(AttributeValueRequestDTO attributeValueRequestDTO, CustomUserPrincipal principal) {
        attributeValueRequestDTO.setValue(attributeValueRequestDTO.getValue().trim().toLowerCase());
//        Optional<AttributeValue> existing = attributeValueRepository.findByValue(attributeValueRequestDTO.getValue());

        attributeTypeRepository.findById(attributeValueRequestDTO.getAttributeTypeId())
                .orElseThrow(() -> new NotFoundException("Attribute type with id '" + attributeValueRequestDTO.getAttributeTypeId()+ "' don't exists"));

        Optional<AttributeValue> existing = attributeValueRepository
                .findByValueAndAttributeTypeId(attributeValueRequestDTO.getValue(), attributeValueRequestDTO.getAttributeTypeId());

        AttributeValue attributeValue;
        if (existing.isPresent() && existing.get().getIsDeleted()) {
            attributeValue = existing.get();
            attributeValue.setIsDeleted(false);
            attributeValue.setDeleteAt(null);
            attributeValueMapper.updateEntityFromDTO(attributeValueRequestDTO, attributeValue);
        } else if (existing.isPresent()) {
            throw new DuplicateResourceException("Attribute value with name '" + attributeValueRequestDTO.getValue() + "' already exists");
        } else {
            attributeValue = attributeValueMapper.toEntity(attributeValueRequestDTO);
        }

        AttributeValue saved = attributeValueRepository.save(attributeValue);
        log.info("Attribute value created: {} by admin: {}", attributeValueRequestDTO.getValue(), principal.getId());
        return attributeValueMapper.toDetailResponseDTO(saved);
    }

    @Override
    public Optional<AttributeValueDetailResponseDTO> getAttributeValueById(Long id) {
        return attributeValueRepository.findById(id).map(attributeValueMapper::toDetailResponseDTO);
    }

    @Override
    public Page<AttributeValueListResponseDTO> searchAllAttributeValues(String search, Boolean isVisual, int page, int size, String sortBy) {
        log.debug("Executing attribute value search");

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

        Specification<AttributeValue> spec = (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            // Buscar por nombre
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        cb.like(cb.lower(root.get("value")), "%" + search.toLowerCase() + "%"));
            }
            if (isVisual != null) {
                predicates.add(
                        cb.equal(root.get("isVisual"), isVisual));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return attributeValueRepository.findAll(spec, pageable).map(attributeValueMapper::toListResponseDTO);
    }

    @Override
    @Transactional
    public AttributeValueDetailResponseDTO updateAttributeValue(Long id, AttributeValueRequestDTO attributeValueRequestDTO, CustomUserPrincipal principal) {
        attributeValueRequestDTO.setValue(attributeValueRequestDTO.getValue().trim().toLowerCase());
        AttributeValue attributeValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attribute value with ID " + id + " not found"));

        attributeValueMapper.updateEntityFromDTO(attributeValueRequestDTO, attributeValue);

        AttributeValue saved = attributeValueRepository.save(attributeValue);
        log.info("Attribute value updated: {} by admin: {}", attributeValue.getValue(), principal.getId());
        return attributeValueMapper.toDetailResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteAttributeValue(Long id, CustomUserPrincipal principal) {
        AttributeValue attributeValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attribute value with ID " + id + " not found"));

        attributeValue.setIsDeleted(true);
        attributeValue.setDeleteAt(LocalDate.now().atStartOfDay());
        attributeValueRepository.save(attributeValue);
        log.info("Attribute value soft deleted: {} by admin: {}", attributeValue.getValue(), principal.getId());
    }

}
