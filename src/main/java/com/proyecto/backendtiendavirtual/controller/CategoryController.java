package com.proyecto.backendtiendavirtual.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backendtiendavirtual.dto.response.CategoryResponseDTO;
import com.proyecto.backendtiendavirtual.service.ICategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/card")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCard() {
        List<CategoryResponseDTO> categories = categoryService.getAllCard();
        return ResponseEntity.ok(categories);
    }

}
