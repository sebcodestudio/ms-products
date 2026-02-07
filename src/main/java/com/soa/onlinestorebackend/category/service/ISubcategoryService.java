package com.soa.onlinestorebackend.category.service;

import java.util.List;

import com.soa.onlinestorebackend.category.dto.response.SubcategoryCardResponseDTO;

public interface ISubcategoryService {

    public List<SubcategoryCardResponseDTO> getAllCard(Long id);

}
