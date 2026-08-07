package com.sebcode.msproducts.complaint.dto.request;

import com.sebcode.msproducts.complaint.entity.ComplaintType;
import com.sebcode.msproducts.complaint.entity.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintRequestDTO {

    @NotNull(message = "El tipo (RECLAMO o QUEJA) es obligatorio")
    private ComplaintType type;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String consumerName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 150, message = "El apellido no puede exceder 150 caracteres")
    private String consumerLastName;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 20, message = "El tipo de documento no puede exceder 20 caracteres")
    private String documentType;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 30, message = "El número de documento no puede exceder 30 caracteres")
    private String documentNumber;

    @NotBlank(message = "El domicilio es obligatorio")
    @Size(max = 255, message = "El domicilio no puede exceder 255 caracteres")
    private String address;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 30, message = "El teléfono no puede exceder 30 caracteres")
    private String phone;

    private Boolean isMinor;

    @Size(max = 255, message = "El nombre del apoderado no puede exceder 255 caracteres")
    private String guardianName;

    @NotNull(message = "El tipo de bien contratado (PRODUCTO o SERVICIO) es obligatorio")
    private ProductType productType;

    @DecimalMin(value = "0.0", inclusive = true, message = "El monto reclamado no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "El monto reclamado no es válido")
    private BigDecimal amountClaimed;

    @NotBlank(message = "La descripción del bien contratado es obligatoria")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String productDescription;

    @NotBlank(message = "El detalle de la reclamación es obligatorio")
    @Size(max = 2000, message = "El detalle no puede exceder 2000 caracteres")
    private String detail;

    @NotBlank(message = "El pedido concreto es obligatorio")
    @Size(max = 1000, message = "El pedido concreto no puede exceder 1000 caracteres")
    private String consumerRequest;

}
