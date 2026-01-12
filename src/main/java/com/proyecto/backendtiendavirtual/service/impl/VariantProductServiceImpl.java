package com.proyecto.backendtiendavirtual.service.impl;

// import com.proyecto.backendtiendavirtual.dto.request.ProductRequestDTO;
// import com.proyecto.backendtiendavirtual.dto.response.ProductCardResponseDTO;
// import com.proyecto.backendtiendavirtual.dto.response.ProductDetailResponseDTO;
import com.proyecto.backendtiendavirtual.dto.response.VariantProductCardResponseDTO;
import com.proyecto.backendtiendavirtual.entity.Brand;
import com.proyecto.backendtiendavirtual.entity.Category;
import com.proyecto.backendtiendavirtual.entity.Product;
import com.proyecto.backendtiendavirtual.entity.Subcategory;
import com.proyecto.backendtiendavirtual.entity.VariantProduct;
// import com.proyecto.backendtiendavirtual.exception.NotFoundException;
// import com.proyecto.backendtiendavirtual.mapper.ProductMapper;
import com.proyecto.backendtiendavirtual.mapper.VariantProductMapper;
// import com.proyecto.backendtiendavirtual.repository.ProductRepository;
import com.proyecto.backendtiendavirtual.repository.VariantProductRepository;
// import com.proyecto.backendtiendavirtual.service.IProductService;
import com.proyecto.backendtiendavirtual.service.IVariantProductService;

import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariantProductServiceImpl implements IVariantProductService {

    private final VariantProductRepository variantProductRepository;
    private final VariantProductMapper variantProductMapper;

    @Override
    public List<VariantProductCardResponseDTO> getAll() {
        return variantProductRepository.findAll()
                .stream()
                .map(variantProductMapper::toResponseCardDTO)
                .collect(toList());
    }

    @Override
    public List<VariantProductCardResponseDTO> getBySold() {
        return variantProductRepository.findBySold(PageRequest.of(0, 5)).stream()
                .map(variantProductMapper::toResponseCardDTO)
                .collect(toList());
    }

    @Override
    public List<VariantProductCardResponseDTO> getByIdCategory(Long idCategory) {
        return variantProductRepository.findByIdCategory(idCategory).stream()
                .map(variantProductMapper::toResponseCardDTO)
                .collect(toList());
    }

    @Override
    public List<VariantProductCardResponseDTO> getDiscountByIdCategory(Long idCategory) {
        return variantProductRepository.findDiscountByIdCategory(idCategory, PageRequest.of(0, 10)).stream()
                .map(variantProductMapper::toResponseCardDTO)
                .collect(toList());
    }

    @Override
    public Page<VariantProductCardResponseDTO> getAllCardFilter(String category, String subcategory, String brand,
            Double score,
            String search, Double minPrice,
            Double maxPrice,
            Pageable pageable) {
        Specification<VariantProduct> spec = Specification.allOf();

        spec = spec.and((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Buscar por nombre
            if (search != null && !search.isEmpty()) {
                Join<VariantProduct, Product> productJoin = root.join("product");
                predicates.add(
                        cb.like(cb.lower(productJoin.get("name")), "%" + search.toLowerCase() + "%"));
            }
            if (score != null && score > 0) {
                Join<VariantProduct, Product> productJoin = root.join("product");
                predicates.add(
                        cb.equal(cb.function("ROUND", Integer.class, productJoin.get("score")), score));
            }
            // Filtrar por marca
            if (brand != null && !brand.isEmpty()) {
                Join<VariantProduct, Product> productJoin = root.join("product");
                Join<Product, Brand> brandJoin = productJoin.join("brand");
                predicates.add(
                        cb.equal(cb.lower(brandJoin.get("name")), brand.toLowerCase()));
            }

            // Filtrar por precio máximo
            if (maxPrice != null && maxPrice > 0) {
                // Join<Product, VariantProduct> variantProductJoin =
                // root.join("variantProducts");
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            // Filtrar por precio mínimo
            if (minPrice != null && minPrice > 0) {
                // Join<Product, VariantProduct> variantProductJoin =
                // root.join("variantProducts");
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            // Filtro por categoría (a través de variantProducts -> subcategories ->
            // category)
            if (subcategory != null && !subcategory.isEmpty()) {
                Join<VariantProduct, Product> productJoin = root.join("product");
                Join<VariantProduct, Subcategory> subcategoryJoin = productJoin.join("subcategories");
                predicates.add(
                        cb.equal(cb.lower(subcategoryJoin.get("name")), subcategory.toLowerCase()));
            }
            if (category != null && !category.isEmpty()) {
                Join<VariantProduct, Product> productJoin = root.join("product");
                Join<VariantProduct, Subcategory> subcategoryJoin = productJoin.join("subcategories");
                Join<Subcategory, Category> categoryJoin = subcategoryJoin.join("category");
                predicates.add(
                        cb.equal(cb.lower(categoryJoin.get("name")), category.toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });

        Page<VariantProduct> variantproducts = variantProductRepository.findAll(spec, pageable);

        return variantproducts.map(variantProductMapper::toResponseCardDTO);
    }

    @Override
    public Optional<VariantProductCardResponseDTO> getProductDetailById(Long id) {
        return variantProductRepository.findById(id).map(variantProductMapper::toResponseCardDTO);
    }

    @Override
    public Optional<VariantProductCardResponseDTO> getById(Long id) {
        return variantProductRepository.findById(id).map(variantProductMapper::toResponseCardDTO);
    }

    // @Override
    // public ProductDetailResponseDTO create(ProductRequestDTO productRequestDTO) {
    // Product product = productMapper.toEntity(productRequestDTO);
    // Product saved = productRepository.save(product);
    // return productMapper.toResponseDTO(saved);
    // }

    // @Override
    // public ProductDetailResponseDTO update(Long id, ProductRequestDTO
    // productRequestDTO) {
    // Product existing = productRepository.findById(id)
    // .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not
    // found"));
    // productMapper.updateEntityFromDTO(productRequestDTO, existing);
    // Product updated = productRepository.save(existing);
    // return productMapper.toResponseDTO(updated);
    // }

    // @Override
    // public boolean delete(Long id) {
    // if (!exists(id))
    // return false;
    // productRepository.deleteById(id);
    // return true;
    // }

    // private boolean exists(Long id) {
    // return productRepository.existsById(id);
    // }

}
