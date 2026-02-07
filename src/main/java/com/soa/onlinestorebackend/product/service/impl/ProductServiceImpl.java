package com.soa.onlinestorebackend.product.service.impl;

import com.soa.onlinestorebackend.exception.NotFoundException;
import com.soa.onlinestorebackend.product.dto.request.ProductRequestDTO;
import com.soa.onlinestorebackend.product.dto.response.ProductDetailResponseDTO;
import com.soa.onlinestorebackend.product.entity.Product;
import com.soa.onlinestorebackend.product.mapper.ProductMapper;
import com.soa.onlinestorebackend.product.repository.ProductRepository;
import com.soa.onlinestorebackend.product.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Optional<ProductDetailResponseDTO> getProductDetailById(Long id) {
        return productRepository.findById(id).map(productMapper::toResponseDetailDTO);
    }

    @Override
    public Optional<ProductDetailResponseDTO> getById(Long id) {
        return productRepository.findById(id).map(productMapper::toResponseDetailDTO);
    }

    @Override
    public ProductDetailResponseDTO create(ProductRequestDTO productRequestDTO) {
        Product product = productMapper.toEntity(productRequestDTO);
        Product saved = productRepository.save(product);
        return productMapper.toResponseDetailDTO(saved);
    }

    @Override
    public ProductDetailResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
        productMapper.updateEntityFromDTO(productRequestDTO, existing);
        Product updated = productRepository.save(existing);
        return productMapper.toResponseDetailDTO(updated);
    }

    @Override
    public boolean delete(Long id) {
        if (!exists(id))
            return false;
        productRepository.deleteById(id);
        return true;
    }

    private boolean exists(Long id) {
        return productRepository.existsById(id);
    }

}


// @Override
// public List<ProductCardResponseDTO> getAll() {
//     return productRepository.findAll()
//             .stream()
//             .map(productMapper::toResponseCardDTO)
//             .collect(toList());
// }

// @Override
// public List<ProductCardResponseDTO> getBySold() {
//     return productRepository.findBySold(PageRequest.of(0, 5)).stream().map(productMapper::toResponseCardDTO)
//             .collect(toList());
// }

// @Override
// public List<ProductCardResponseDTO> getByIdCategory(Long idCategory) {
//     return productRepository.findByIdCategory(idCategory, PageRequest.of(0, 10)).stream()
//             .map(productMapper::toResponseCardDTO)
//             .collect(toList());
// }

// @Override
// public List<ProductCardResponseDTO> getDiscountByIdCategory(Long idCategory) {
//     return productRepository.findDiscountByIdCategory(idCategory, PageRequest.of(0, 10)).stream()
//             .map(productMapper::toResponseCardDTO)
//             .collect(toList());
// }

// @Override
// public Page<ProductCardResponseDTO> getAllCardFilter(String category, String subcategory, String brand,
//         Double score,
//         String search, Double minPrice,
//         Double maxPrice,
//         Pageable pageable) {
//     Specification<Product> spec = Specification.allOf();

//     spec = spec.and((root, query, cb) -> {
//         List<Predicate> predicates = new ArrayList<>();

//         // Buscar por nombre
//         if (search != null && !search.isEmpty()) {
//             predicates.add(
//                     cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
//         }

//         if (score != null && score > 0) {
//             predicates.add(
//                     cb.equal(cb.function("ROUND", Integer.class, root.get("score")), score));
//         }
//         // Filtrar por marca
//         if (brand != null && !brand.isEmpty()) {
//             Join<Product, Brand> brandJoin = root.join("brand");
//             predicates.add(
//                     cb.equal(cb.lower(brandJoin.get("name")), brand.toLowerCase()));
//         }

//         // Filtrar por precio máximo
//         if (maxPrice != null && maxPrice > 0) {
//             Join<Product, VariantProduct> variantProductJoin = root.join("variantProducts");
//             predicates.add(
//                     cb.lessThanOrEqualTo(variantProductJoin.get("price"), maxPrice));
//         }

//         // Filtrar por precio mínimo
//         if (minPrice != null && minPrice > 0) {
//             Join<Product, VariantProduct> variantProductJoin = root.join("variantProducts");
//             predicates.add(
//                     cb.greaterThanOrEqualTo(variantProductJoin.get("price"), minPrice));
//         }

//         // Filtro por categoría (a través de variantProducts -> subcategories ->
//         // category)
//         if (subcategory != null && !subcategory.isEmpty()) {
//             Join<Product, VariantProduct> variantProductJoin = root.join("variantProducts");
//             Join<VariantProduct, Subcategory> subcategoryJoin = variantProductJoin.join("subcategories");
//             predicates.add(
//                     cb.equal(cb.lower(subcategoryJoin.get("name")), subcategory.toLowerCase()));
//         }
//         if (category != null && !category.isEmpty()) {
//             Join<Product, VariantProduct> variantProductJoin = root.join("variantProducts");
//             Join<VariantProduct, Subcategory> subcategoryJoin = variantProductJoin.join("subcategories");
//             Join<Subcategory, Category> categoryJoin = subcategoryJoin.join("category");
//             predicates.add(
//                     cb.equal(cb.lower(categoryJoin.get("name")), category.toLowerCase()));
//         }

//         return cb.and(predicates.toArray(new Predicate[0]));
//     });

//     Page<Product> products = productRepository.findAll(spec, pageable);

//     return products.map(productMapper::toResponseCardDTO);
// }

