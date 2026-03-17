package com.sebcode.msproducts.category.service.impl;

import com.sebcode.msproducts.category.dto.request.SubcategoryRequestDTO;
import com.sebcode.msproducts.category.dto.response.SubcategoryDetailResponseDTO;
import com.sebcode.msproducts.category.dto.response.SubcategoryListResponseDTO;
import com.sebcode.msproducts.category.entity.Subcategory;
import com.sebcode.msproducts.category.mapper.SubcategoryMapper;
import com.sebcode.msproducts.category.repository.CategoryRepository;
import com.sebcode.msproducts.category.repository.SubcategoryRepository;
import com.sebcode.msproducts.category.service.ISubcategoryService;
import com.sebcode.msproducts.exception.NotFoundException;
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

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements ISubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryMapper subcategoryMapper;

    @Override
    @Transactional
    public SubcategoryDetailResponseDTO createSubcategory(SubcategoryRequestDTO subcategoryRequestDTO, CustomUserPrincipal principal) {
        subcategoryRequestDTO.setName(subcategoryRequestDTO.getName().trim().toLowerCase());
//        Optional<Subcategory> existing = subcategoryRepository.findByValue(subcategoryRequestDTO.getValue());

        categoryRepository.findById(subcategoryRequestDTO.getCategoryId()).orElseThrow(() -> new NotFoundException("Category with id '" + subcategoryRequestDTO.getCategoryId() + "' don't exists"));

        Optional<Subcategory> existing = subcategoryRepository.findByNameAndCategoryId(subcategoryRequestDTO.getName(), subcategoryRequestDTO.getCategoryId());

        Subcategory subcategory;
        if (existing.isPresent() && existing.get().getIsDeleted()) {
            subcategory = existing.get();
            subcategory.setIsDeleted(false);
            subcategory.setDeleteAt(null);
            subcategoryMapper.updateEntityFromDTO(subcategoryRequestDTO, subcategory);
        } else if (existing.isPresent()) {
            throw new NotFoundException("Subcategory with name '" + subcategoryRequestDTO.getName() + "' already exists");
        } else {
            subcategory = subcategoryMapper.toEntity(subcategoryRequestDTO);
        }

        Subcategory saved = subcategoryRepository.save(subcategory);
        log.info("Subcategory created: {} by admin: {}", subcategoryRequestDTO.getName(), principal.getId());
        return subcategoryMapper.toDetailResponseDTO(saved);
    }

    @Override
    public Optional<SubcategoryDetailResponseDTO> getSubcategoryById(Long id) {
        return subcategoryRepository.findById(id).map(subcategoryMapper::toDetailResponseDTO);
    }

    @Override
    public List<SubcategoryListResponseDTO> getAllList(Long id) {
        List<Subcategory> subcategories = subcategoryRepository.findAllList(id, PageRequest.of(0, 10));
        return subcategories.stream().map(subcategoryMapper::toListResponseDTO).collect(toList());
    }

    @Override
    public Page<SubcategoryListResponseDTO> searchAllSubcategories(String search, Boolean isVisual, int page, int size, String sortBy) {
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
        Sort sort = sortDirection.equalsIgnoreCase(valDesc) ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Subcategory> spec = (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            // Buscar por nombre
            if (search != null && !search.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
            }
            if (isVisual != null) {
                predicates.add(cb.equal(root.get("isVisual"), isVisual));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return subcategoryRepository.findAll(spec, pageable).map(subcategoryMapper::toListResponseDTO);
    }

    @Override
    @Transactional
    public SubcategoryDetailResponseDTO updateSubcategory(Long id, SubcategoryRequestDTO subcategoryRequestDTO, CustomUserPrincipal principal) {
        subcategoryRequestDTO.setName(subcategoryRequestDTO.getName().trim().toLowerCase());
        Subcategory subcategory = subcategoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Subcategory with ID " + id + " not found"));

        subcategoryMapper.updateEntityFromDTO(subcategoryRequestDTO, subcategory);

        Subcategory saved = subcategoryRepository.save(subcategory);
        log.info("Subcategory updated: {} by admin: {}", subcategory.getName(), principal.getId());
        return subcategoryMapper.toDetailResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteSubcategory(Long id, CustomUserPrincipal principal) {
        Subcategory subcategory = subcategoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Subcategory with ID " + id + " not found"));

        subcategory.setIsDeleted(true);
        subcategory.setDeleteAt(LocalDate.now().atStartOfDay());
        subcategoryRepository.save(subcategory);
        log.info("Subcategory soft deleted: {} by admin: {}", subcategory.getName(), principal.getId());
    }

}
