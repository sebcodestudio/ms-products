package com.proyecto.backendtiendavirtual.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.proyecto.backendtiendavirtual.dto.response.SubcategoryCardResponseDTO;
import com.proyecto.backendtiendavirtual.entity.Subcategory;
import com.proyecto.backendtiendavirtual.mapper.SubcategoryMapper;
import com.proyecto.backendtiendavirtual.repository.SubcategoryRepository;
import com.proyecto.backendtiendavirtual.service.ISubcategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements ISubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final SubcategoryMapper subcategoryMapper;

    @Override
    public List<SubcategoryCardResponseDTO> getAllCard(Long id) {
        List<Subcategory> subcategories = subcategoryRepository.findAllCard(id, PageRequest.of(0, 10));
        return subcategories.stream().map(subcategoryMapper::toResponseCardDTO).collect(toList());
    }

}
