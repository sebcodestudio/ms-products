package com.proyecto.backendtiendavirtual.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backendtiendavirtual.dto.response.SubcategoryCardResponseDTO;
import com.proyecto.backendtiendavirtual.service.ISubcategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/subcategories")
@RequiredArgsConstructor
public class SubcategoryController {

    private final ISubcategoryService subcategoryService;

    @GetMapping("/card/{id}")
    public ResponseEntity<List<SubcategoryCardResponseDTO>> getAllCard(@PathVariable Long id) {
        List<SubcategoryCardResponseDTO> subcategories = subcategoryService.getAllCard(id);
        return ResponseEntity.ok(subcategories);
    }

}
