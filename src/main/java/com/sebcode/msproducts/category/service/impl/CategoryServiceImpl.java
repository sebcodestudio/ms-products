package com.sebcode.msproducts.category.service.impl;

import com.sebcode.msproducts.category.dto.request.CategoryRequestDTO;
import com.sebcode.msproducts.category.dto.response.CategoryDetailResponseDTO;
import com.sebcode.msproducts.category.dto.response.CategoryListResponseDTO;
import com.sebcode.msproducts.category.entity.Category;
import com.sebcode.msproducts.category.mapper.CategoryMapper;
import com.sebcode.msproducts.category.repository.CategoryRepository;
import com.sebcode.msproducts.category.service.ICategoryService;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDetailResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO, CustomUserPrincipal principal) {
        categoryRequestDTO.setName(categoryRequestDTO.getName().trim().toLowerCase());
//        Optional<Category> existing = categoryRepository.findByValue(categoryRequestDTO.getValue());

//        attributeTypeRepository.findById(categoryRequestDTO.getAttributeTypeId())
//                .orElseThrow(() -> new NotFoundException("Attribute type with id '" + categoryRequestDTO.getAttributeTypeId()+ "' don't exists"));

        Optional<Category> existing = categoryRepository.findByName(categoryRequestDTO.getName());

        Category category;
        if (existing.isPresent() && existing.get().getIsDeleted()) {
            category = existing.get();
            category.setIsDeleted(false);
            category.setDeleteAt(null);
            categoryMapper.updateEntityFromDTO(categoryRequestDTO, category);
        } else if (existing.isPresent()) {
            throw new NotFoundException("Category with name '" + categoryRequestDTO.getName() + "' already exists");
        } else {
            category = categoryMapper.toEntity(categoryRequestDTO);
        }

        Category saved = categoryRepository.save(category);
        log.info("Category created: {} by admin: {}", categoryRequestDTO.getName(), principal.getId());
        return categoryMapper.toDetailResponseDTO(saved);
    }

    @Override
    public Optional<CategoryDetailResponseDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDetailResponseDTO);
    }

    @Override
    public List<CategoryListResponseDTO> getAllList() {
        List<Category> categories = categoryRepository.findAllList(PageRequest.of(0, 10));
        return categories.stream().map(categoryMapper::toListResponseDTO).collect(toList());
    }

    @Override
    public Page<CategoryListResponseDTO> searchAllCategorys(String search, Boolean isVisual, int page, int size, String sortBy) {
        log.debug("Executing category search");

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

        Specification<Category> spec = (root, query, cb) -> {
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

        return categoryRepository.findAll(spec, pageable).map(categoryMapper::toListResponseDTO);
    }

    @Override
    @Transactional
    public CategoryDetailResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO, CustomUserPrincipal principal) {
        categoryRequestDTO.setName(categoryRequestDTO.getName().trim().toLowerCase());
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));

        categoryMapper.updateEntityFromDTO(categoryRequestDTO, category);

        Category saved = categoryRepository.save(category);
        log.info("Category updated: {} by admin: {}", category.getName(), principal.getId());
        return categoryMapper.toDetailResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id, CustomUserPrincipal principal) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));

        category.setIsDeleted(true);
        category.setDeleteAt(LocalDate.now().atStartOfDay());
        categoryRepository.save(category);
        log.info("Category soft deleted: {} by admin: {}", category.getName(), principal.getId());
    }

}
