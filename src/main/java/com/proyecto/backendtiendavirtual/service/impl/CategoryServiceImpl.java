package com.proyecto.backendtiendavirtual.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.proyecto.backendtiendavirtual.dto.response.CategoryResponseDTO;
import com.proyecto.backendtiendavirtual.entity.Category;
import com.proyecto.backendtiendavirtual.mapper.CategoryMapper;
import com.proyecto.backendtiendavirtual.repository.CategoryRepository;
import com.proyecto.backendtiendavirtual.service.ICategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDTO> getAllCard() {
        List<Category> categories = categoryRepository.findAllCard(PageRequest.of(0, 10));
        return categories.stream().map(categoryMapper::toResponseDTO).collect(toList());
    }

}
