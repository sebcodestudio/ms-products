package com.soa.onlinestorebackend.product.controller;

import com.soa.onlinestorebackend.product.dto.request.ProductRequestDTO;
import com.soa.onlinestorebackend.product.dto.response.ProductDetailResponseDTO;
import com.soa.onlinestorebackend.product.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @PostMapping
    public ResponseEntity<ProductDetailResponseDTO> create(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        log.info("POST /api/v1/products - Creating product: {}", productRequestDTO.getName());
        ProductDetailResponseDTO createdProduct = productService.create(productRequestDTO);
        URI location = URI.create(String.format("/api/v1/products/%d", createdProduct.getId()));
        return ResponseEntity.created(location).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailResponseDTO> update(@PathVariable Long id,
                                                           @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        log.info("PUT /api/v1/products/{} - Updating product", id);
        ProductDetailResponseDTO updatedProduct = productService.update(id, productRequestDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/products/{} - Deleting product", id);
        boolean deleted = productService.delete(id);
        if (deleted) {
            log.info("Product with ID {} successfully deleted", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Product with ID {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }

}

// @GetMapping
// public ResponseEntity<List<ProductCardResponseDTO>> getAll() {
//     log.info("⏳ Entrando al endpoint GET /api/v1/products/all");
//     List<ProductCardResponseDTO> products = productService.getAll();
//     log.info("✅ Productos obtenidos correctamente: {}", products.size());
//     return ResponseEntity.ok(products);
// }

// @GetMapping("/best-sellers")
// public ResponseEntity<List<ProductCardResponseDTO>> getBySold() {
//     List<ProductCardResponseDTO> products = productService.getBySold();
//     return ResponseEntity.ok(products);
// }

// @GetMapping("/by-category/{id}")
// public ResponseEntity<List<ProductCardResponseDTO>> getByIdCategory(@PathVariable Long id) {
//     List<ProductCardResponseDTO> products = productService.getByIdCategory(id);
//     return ResponseEntity.ok(products);
// }

// @GetMapping("/discount-by-category/{id}")
// public ResponseEntity<List<ProductCardResponseDTO>> getDiscountByIdCategory(@PathVariable Long id) {
//     List<ProductCardResponseDTO> products = productService.getDiscountByIdCategory(id);
//     return ResponseEntity.ok(products);
// }

// @GetMapping("/filter")
// public ResponseEntity<Page<ProductCardResponseDTO>> getAllFilter(
//         @RequestParam(required = false) String search,
//         @RequestParam(required = false) String brand,
//         @RequestParam(required = false) Double score,
//         @RequestParam(required = false) String category,
//         @RequestParam(required = false) String subcategory,
//         @RequestParam(required = false) Double minPrice,
//         @RequestParam(required = false) Double maxPrice,
//         @RequestParam(defaultValue = "0") int page,
//         @RequestParam(defaultValue = "10") int size,
//         @RequestParam(defaultValue = "idProductAsc") String sortBy) {
//     String valAsc = "Asc";
//     String valDesc = "Desc";
//     String sortField = "";
//     String sortDirection = "";
//     if (sortBy.contains(valAsc)) {
//         sortField = sortBy.replace(valAsc, "");
//         sortDirection = valAsc;
//     }
//     if (sortBy.contains(valDesc)) {
//         sortField = sortBy.replace(valDesc, "");
//         sortDirection = valDesc;
//     }

//     Sort sort = sortDirection.equalsIgnoreCase(valDesc) ? Sort.by(sortField).descending()
//             : Sort.by(sortField).ascending();

//     Pageable pageable = PageRequest.of(page, size, sort);
//     Page<ProductCardResponseDTO> products = productService.getAllCardFilter(category, subcategory, brand, score,
//             search,
//             minPrice, maxPrice, pageable);
//     return ResponseEntity.ok(products);
// }

//    @GetMapping("/detail/{id}")
//    public ResponseEntity<ProductDetailResponseDTO> getProductDetailById(@PathVariable Long id) {
//        log.info("GET /api/v1/products/{} - Retrieving product by ID", id);
//        return productService.getProductDetailById(id)
//                .map(ResponseEntity::ok)
//                .orElseThrow(() -> {
//                    log.warn("Product with ID {} not found", id);
//                    return new NotFoundException("Product with ID " + id + " not found");
//                });
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductDetailResponseDTO> getById(@PathVariable Long id) {
//        log.info("GET /api/v1/products/{} - Retrieving product by ID", id);
//        return productService.getById(id)
//                .map(ResponseEntity::ok)
//                .orElseThrow(() -> {
//                    log.warn("Product with ID {} not found", id);
//                    return new NotFoundException("Product with ID " + id + " not found");
//                });
//    }

