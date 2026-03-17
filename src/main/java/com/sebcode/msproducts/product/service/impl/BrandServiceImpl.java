package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.BrandRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.BrandDetailResponseDTO;
import com.sebcode.msproducts.product.dto.response.admin.BrandListResponseDTO;
import com.sebcode.msproducts.product.entity.Brand;
import com.sebcode.msproducts.product.mapper.BrandMapper;
import com.sebcode.msproducts.product.repository.BrandRepository;
import com.sebcode.msproducts.product.service.IBrandService;
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
public class BrandServiceImpl implements IBrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    @Transactional
    public BrandDetailResponseDTO createBrand(BrandRequestDTO brandRequestDTO, CustomUserPrincipal principal) {
        brandRequestDTO.setName(brandRequestDTO.getName().trim().toLowerCase());
        Optional<Brand> existing = brandRepository.findByName(brandRequestDTO.getName());

        Brand brand;
        if (existing.isPresent() && existing.get().getIsDeleted()) {
            brand = existing.get();
            brand.setIsDeleted(false);
            brand.setDeleteAt(null);
            brandMapper.updateEntityFromDTO(brandRequestDTO, brand);
        } else if (existing.isPresent()) {
            throw new NotFoundException("Brand with name '" + brandRequestDTO.getName() + "' already exists");
        } else {
            brand = brandMapper.toEntity(brandRequestDTO);
        }

        Brand saved = brandRepository.save(brand);
        log.info("Brand created: {} by admin: {}", brandRequestDTO.getName(), principal.getId());
        return brandMapper.toDetailResponseDTO(saved);
    }

    @Override
    public Optional<BrandDetailResponseDTO> getBrandById(Long id) {
        return brandRepository.findById(id).map(brandMapper::toDetailResponseDTO);
    }

    @Override
    public Page<BrandListResponseDTO> searchAllBrands(String search, Boolean isVisual, int page, int size, String sortBy) {
        log.debug("Executing brand search");

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

        Specification<Brand> spec = (root, query, cb) -> {
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

        return brandRepository.findAll(spec, pageable).map(brandMapper::toListResponseDTO);
    }

    @Override
    @Transactional
    public BrandDetailResponseDTO updateBrand(Long id, BrandRequestDTO brandRequestDTO, CustomUserPrincipal principal) {
        brandRequestDTO.setName(brandRequestDTO.getName().trim().toLowerCase());
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand with ID " + id + " not found"));

        brandMapper.updateEntityFromDTO(brandRequestDTO, brand);

        Brand saved = brandRepository.save(brand);
        log.info("Brand updated: {} by admin: {}", brand.getName(), principal.getId());
        return brandMapper.toDetailResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id, CustomUserPrincipal principal) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand with ID " + id + " not found"));

        brand.setIsDeleted(true);
        brand.setDeleteAt(LocalDate.now().atStartOfDay());
        brandRepository.save(brand);
        log.info("Brand soft deleted: {} by admin: {}", brand.getName(), principal.getId());
    }

}
