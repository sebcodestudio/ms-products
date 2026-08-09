package com.sebcode.msproducts.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String customerName;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(max = 30, message = "El telefono no puede exceder 30 caracteres")
    private String customerPhone;

    @Email(message = "El email no es valido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String customerEmail;

    @NotBlank(message = "La direccion es obligatoria")
    @Size(max = 500, message = "La direccion no puede exceder 500 caracteres")
    private String customerAddress;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<OrderItemRequestDTO> items;

}
