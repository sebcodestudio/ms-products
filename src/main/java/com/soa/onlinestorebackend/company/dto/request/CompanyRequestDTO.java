package com.soa.onlinestorebackend.company.dto.request;

import com.soa.onlinestorebackend.company.enums.TaxRegime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyRequestDTO {

    @NotBlank(message = "Legal name is required")
    @Size(max = 200, message = "Legal name must not exceed 200 characters")
    private String legalName;

    @NotBlank(message = "Trade name is required")
    @Size(max = 200, message = "Trade name must not exceed 200 characters")
    private String tradeName;

    @NotBlank(message = "RUC is required")
    @Pattern(regexp = "^(10|15|17|20)\\d{9}$", message = "Invalid RUC format")
    private String ruc;

    @NotBlank(message = "Fiscal Address is required")
    @Size(max = 500, message = "Fiscal address must not exceed 500 characters")
    private String fiscalAddress;

    @NotNull(message = "Tax regime is required")
    private TaxRegime taxRegime;

}
