package com.proyecto.backendtiendavirtual.service;

import java.util.List;

import com.proyecto.backendtiendavirtual.dto.response.SubcategoryCardResponseDTO;

public interface ISubcategoryService {

    public List<SubcategoryCardResponseDTO> getAllCard(Long id);

}
