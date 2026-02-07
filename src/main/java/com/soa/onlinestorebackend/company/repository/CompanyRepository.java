package com.soa.onlinestorebackend.company.repository;

import com.soa.onlinestorebackend.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    boolean existsByRuc(String ruc);

    boolean existsByLegalName(String legalName);

    boolean existsByTradeName(String tradeName);

    Optional<Company> findByRuc(String ruc);

    Optional<Company> findByIdAndIsDeletedFalse(Long id);

}
