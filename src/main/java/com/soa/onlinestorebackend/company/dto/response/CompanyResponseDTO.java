package com.soa.onlinestorebackend.company.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soa.onlinestorebackend.company.enums.ContributorType;
import com.soa.onlinestorebackend.company.enums.TaxRegime;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CompanyResponseDTO {

    private Long id;
    private String legalName;
    private String tradeName;
    private String ruc;
    private ContributorType contributorType; // ✅ Agregado
    private String fiscalAddress;
    private TaxRegime taxRegime; // ✅ Agregado
    private Boolean active; // ✅ Agregado
    private LocalDateTime createdAt; // ✅ Opcional pero útil

    @JsonIgnore
    public String getContributorTypeDescription() {
        return contributorType != null ? contributorType.getDescription() : null;
    }

    @JsonIgnore
    public String getTaxRegimeDescription() {
        return taxRegime != null ? taxRegime.name() : null;
    }

}
