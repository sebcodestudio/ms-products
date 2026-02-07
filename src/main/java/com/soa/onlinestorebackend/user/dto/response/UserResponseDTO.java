package com.soa.onlinestorebackend.user.dto.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    
}
