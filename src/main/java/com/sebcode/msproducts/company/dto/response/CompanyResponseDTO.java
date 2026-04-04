package com.sebcode.msproducts.company.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sebcode.msproducts.company.enums.ContributorType;
import com.sebcode.msproducts.company.enums.TaxRegime;
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
    private Boolean state; // ✅ Agregado
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
