package com.sebcode.msproducts.company.mapper;

import com.sebcode.msproducts.company.dto.request.CompanyRequestDTO;
import com.sebcode.msproducts.company.dto.response.CompanyResponseDTO;
import com.sebcode.msproducts.company.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contributorType", ignore = true)
    @Mapping(target = "state", constant = "true")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "companyUsers", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    Company toEntity(CompanyRequestDTO companyRequestDTO);

    CompanyResponseDTO toResponseDTO(Company company);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contributorType", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "companyUsers", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    void updateEntityFromDTO(CompanyRequestDTO companyRequestDTO, @MappingTarget Company company);

}
