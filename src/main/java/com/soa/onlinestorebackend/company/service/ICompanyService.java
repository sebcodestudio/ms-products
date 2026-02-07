package com.soa.onlinestorebackend.company.service;

import com.soa.onlinestorebackend.company.dto.request.CompanyRequestDTO;
import com.soa.onlinestorebackend.company.dto.response.CompanyResponseDTO;
import com.soa.onlinestorebackend.company.enums.CompanySort;
import com.soa.onlinestorebackend.security.config.security.CustomUserPrincipal;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ICompanyService {

    CompanyResponseDTO createCompany(CompanyRequestDTO companyRequestDTO);

    CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO companyRequestDTO);

    void deleteCompany(Long id);

    CompanyResponseDTO getById(Long id);

    Page<CompanyResponseDTO> searchCompanies(String search, Boolean active, int page, int size, CompanySort sortBy);

}
