package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.VariantAttributeRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantAttributeDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantAttributeListResponseDTO;
import com.sebcode.msproducts.product.entity.VariantAttribute;
import com.sebcode.msproducts.product.mapper.VariantAttributeMapper;
import com.sebcode.msproducts.product.repository.AttributeTypeRepository;
import com.sebcode.msproducts.product.repository.AttributeValueRepository;
import com.sebcode.msproducts.product.repository.VariantAttributeRepository;
import com.sebcode.msproducts.product.repository.VariantProductRepository;
import com.sebcode.msproducts.product.service.IVariantAttributeService;
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
public class VariantAttributeServiceImpl implements IVariantAttributeService {

    private final VariantAttributeRepository variantAttributeRepository;
    private final VariantProductRepository variantProductRepository;
    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final VariantAttributeMapper variantAttributeMapper;

    @Override
    @Transactional
    public VariantAttributeDetailResponseDTO createVariantAttribute(VariantAttributeRequestDTO variantAttributeRequestDTO, CustomUserPrincipal principal) {
        variantProductRepository.findById(variantAttributeRequestDTO.getVariantProductId())
                .orElseThrow(() -> new NotFoundException("Variant attribute with id '" + variantAttributeRequestDTO.getVariantProductId()+ "' don't exists"));
        attributeTypeRepository.findById(variantAttributeRequestDTO.getAttributeTypeId())
                .orElseThrow(() -> new NotFoundException("Variant attribute with id '" + variantAttributeRequestDTO.getAttributeTypeId()+ "' don't exists"));
        attributeValueRepository.findById(variantAttributeRequestDTO.getAttributeValueId())
                .orElseThrow(() -> new NotFoundException("Variant attribute with id '" + variantAttributeRequestDTO.getAttributeValueId()+ "' don't exists"));

//        Optional<VariantAttribute> existing = variantAttributeRepository
//                .findByValueAndAttributeTypeId(variantAttributeRequestDTO.getValue(), variantAttributeRequestDTO.getAttributeTypeId());

//        VariantAttribute variantAttribute;
//        if (existing.isPresent() && existing.get().getIsDeleted()) {
//            variantAttribute = existing.get();
//            variantAttribute.setIsDeleted(false);
//            variantAttribute.setDeleteAt(null);
//            variantAttributeMapper.updateEntityFromDTO(variantAttributeRequestDTO, variantAttribute);
//        } else if (existing.isPresent()) {
//            throw new NotFoundException("Attribute value with name '" + variantAttributeRequestDTO.getValue() + "' already exists");
//        } else {
//            variantAttribute = variantAttributeMapper.toEntity(variantAttributeRequestDTO);
//        }

        VariantAttribute saved = variantAttributeRepository.save(variantAttributeMapper.toEntity(variantAttributeRequestDTO));
        log.info("Variant attribute created: by admin: {}", principal.getId());
        return variantAttributeMapper.toDetailResponseDTO(saved);
    }

    @Override
    public Optional<VariantAttributeDetailResponseDTO> getVariantAttributeById(Long id) {
        return variantAttributeRepository.findById(id).map(variantAttributeMapper::toDetailResponseDTO);
    }

    @Override
    public Page<VariantAttributeListResponseDTO> searchAllVariantAttributes(String search, Boolean isVisual, int page, int size, String sortBy) {
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

        Specification<VariantAttribute> spec = (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            // Buscar por nombre
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        cb.like(cb.lower(root.get("value")), "%" + search.toLowerCase() + "%"));
            }
//            if (isVisual != null) {
//                predicates.add(
//                        cb.equal(root.get("isVisual"), isVisual));
//            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return variantAttributeRepository.findAll(spec, pageable).map(variantAttributeMapper::toListResponseDTO);
    }

    @Override
    @Transactional
    public VariantAttributeDetailResponseDTO updateVariantAttribute(Long id, VariantAttributeRequestDTO variantAttributeRequestDTO, CustomUserPrincipal principal) {
//        variantAttributeRequestDTO.setValue(variantAttributeRequestDTO.getValue().trim().toLowerCase());
        VariantAttribute variantAttribute = variantAttributeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Variant attribute with ID " + id + " not found"));

        variantProductRepository.findById(variantAttributeRequestDTO.getVariantProductId())
                .orElseThrow(() -> new NotFoundException("Variant attribute with id '" + variantAttributeRequestDTO.getVariantProductId()+ "' don't exists"));
        attributeTypeRepository.findById(variantAttributeRequestDTO.getAttributeTypeId())
                .orElseThrow(() -> new NotFoundException("Variant attribute with id '" + variantAttributeRequestDTO.getAttributeTypeId()+ "' don't exists"));
        attributeValueRepository.findById(variantAttributeRequestDTO.getAttributeValueId())
                .orElseThrow(() -> new NotFoundException("Variant attribute with id '" + variantAttributeRequestDTO.getAttributeValueId()+ "' don't exists"));

        variantAttributeMapper.updateEntityFromDTO(variantAttributeRequestDTO, variantAttribute);

        VariantAttribute saved = variantAttributeRepository.save(variantAttribute);
        log.info("Variant attribute updated: by admin: {}", principal.getId());
        return variantAttributeMapper.toDetailResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteVariantAttribute(Long id, CustomUserPrincipal principal) {
        VariantAttribute variantAttribute = variantAttributeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Variant attribute with ID " + id + " not found"));

        variantAttribute.setIsDeleted(true);
        variantAttribute.setDeleteAt(LocalDate.now().atStartOfDay());
        variantAttributeRepository.save(variantAttribute);
        log.info("Variant attribute soft deleted: {} by admin: {}", variantAttribute.getValue(), principal.getId());
    }

}
