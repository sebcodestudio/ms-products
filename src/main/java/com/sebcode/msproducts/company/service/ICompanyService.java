package com.sebcode.msproducts.company.service;

import com.sebcode.msproducts.company.dto.request.CompanyRequestDTO;
import com.sebcode.msproducts.company.dto.response.CompanyResponseDTO;
import com.sebcode.msproducts.company.enums.CompanySort;
import org.springframework.data.domain.Page;

public interface ICompanyService {

    CompanyResponseDTO createCompany(CompanyRequestDTO companyRequestDTO);

    CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO companyRequestDTO);

    void deleteCompany(Long id);

    CompanyResponseDTO getById(Long id);

    Page<CompanyResponseDTO> searchCompanies(String search, Boolean active, int page, int size, CompanySort sortBy);

}
