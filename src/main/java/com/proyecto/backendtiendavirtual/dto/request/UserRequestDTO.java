package com.proyecto.backendtiendavirtual.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

}
