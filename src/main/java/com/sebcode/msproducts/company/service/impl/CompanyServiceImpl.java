package com.sebcode.msproducts.company.service.impl;

import com.sebcode.msproducts.company.dto.request.CompanyRequestDTO;
import com.sebcode.msproducts.company.dto.response.CompanyResponseDTO;
import com.sebcode.msproducts.company.entity.Company;
import com.sebcode.msproducts.company.entity.CompanyUser;
import com.sebcode.msproducts.company.enums.CompanySort;
import com.sebcode.msproducts.company.mapper.CompanyMapper;
import com.sebcode.msproducts.company.repository.CompanyRepository;
import com.sebcode.msproducts.company.service.ICompanyService;
import com.sebcode.msproducts.exception.BadRequestException;
import com.sebcode.msproducts.exception.DuplicateResourceException;
import com.sebcode.msproducts.exception.NotFoundException;
import jakarta.annotation.Nullable;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyResponseDTO createCompany(CompanyRequestDTO dto) {
        log.info("Creating company: {}", dto.getLegalName());

        validateUniqueness(dto, null);

        Company company = companyMapper.toEntity(dto);
        Company saved = companyRepository.save(company);

        log.info("✅ Company created - ID: {}, RUC: {}", saved.getId(), saved.getRuc());

        return companyMapper.toResponseDTO(saved);
    }

    @Override
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO dto) {
        log.info("Updating company ID: {}", id);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company with ID " + id + " not found"));

        validateUniqueness(dto, company);

        companyMapper.updateEntityFromDTO(dto, company);
        Company updated = companyRepository.save(company);

        log.info("✅ Company updated - ID: {}, RUC: {}", updated.getId(), updated.getRuc());

        return companyMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteCompany(Long id) {
        log.info("Deleting company ID: {}", id);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company with ID " + id + " not found"));

        validateCanDelete(company);

        company.setIsDeleted(true);
//        company.setDeleteAt(LocalDate.now());
        company.setState(false);
        companyRepository.save(company);

        log.info("✅ Company soft deleted - ID: {}, RUC: {}", company.getId(), company.getRuc());
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponseDTO getById(Long id) {
        log.debug("Fetching company by ID: {}", id);

        return companyRepository.findByIdAndIsDeletedFalse(id)
                .map(companyMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.warn("Company with ID {} not found", id);
                    return new NotFoundException("Company with ID " + id + " not found");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyResponseDTO> searchCompanies(
            @Nullable String search,
            @Nullable Boolean active,
            int page,
            int size,
            @Nullable CompanySort sortBy) {

        log.debug("Searching companies - search: {}, active: {}, page: {}, size: {}",
                search, active, page, size);

        Sort sort = sortBy != null ? sortBy.toSort() : Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Company> spec = (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (active != null) {
                predicates.add(cb.equal(root.get("active"), active));
            }

            if (search != null && !search.isEmpty()) {
                String searchLower = "%" + search.toLowerCase() + "%";
                Predicate legalNamePredicate = cb.like(cb.lower(root.get("legalName")), searchLower);
                Predicate tradeNamePredicate = cb.like(cb.lower(root.get("tradeName")), searchLower);
                Predicate rucPredicate = cb.like(root.get("ruc"), "%" + search + "%");

                predicates.add(cb.or(legalNamePredicate, tradeNamePredicate, rucPredicate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Company> companies = companyRepository.findAll(spec, pageable);

        log.debug("Found {} companies", companies.getTotalElements());

        return companies.map(companyMapper::toResponseDTO);
    }

    // ========== PRIVATE HELPER METHODS ==========

    private void validateCanDelete(Company company) {
        long activeUsers = company.getCompanyUsers().stream()
                .filter(CompanyUser::getState)
                .count();

        if (activeUsers > 0) {
            throw new BadRequestException(
                    "Cannot delete company with " + activeUsers + " active user(s). " +
                            "Please remove all users first."
            );
        }
    }

    private void validateUniqueness(CompanyRequestDTO dto, @Nullable Company current) {
        if (current == null || !current.getRuc().equals(dto.getRuc())) {
            if (companyRepository.existsByRuc(dto.getRuc())) {
                throw new DuplicateResourceException("Company with RUC " + dto.getRuc() + " already exists");
            }
        }

        if (current == null || !current.getLegalName().equals(dto.getLegalName())) {
            if (companyRepository.existsByLegalName(dto.getLegalName())) {
                throw new DuplicateResourceException("Company with legal name " + dto.getLegalName() + " already exists");
            }
        }

        if (current == null || !current.getTradeName().equals(dto.getTradeName())) {
            if (companyRepository.existsByTradeName(dto.getTradeName())) {
                throw new DuplicateResourceException("Company with trade name " + dto.getTradeName() + " already exists");
            }
        }
    }

}
