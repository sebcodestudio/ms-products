package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.category.repository.SubcategoryRepository;
import com.sebcode.msproducts.exception.DuplicateResourceException;
import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.ProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.ProductDetailResponseDTO;
import com.sebcode.msproducts.product.entity.Brand;
import com.sebcode.msproducts.product.entity.Product;
import com.sebcode.msproducts.product.mapper.ProductMapper;
import com.sebcode.msproducts.product.repository.BrandRepository;
import com.sebcode.msproducts.product.repository.ProductRepository;
import com.sebcode.msproducts.product.service.impl.ProductServiceImpl;
import com.sebcode.msproducts.security.config.security.CustomUserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ProductServiceImpl")
class ProductServiceImplTest {

    @Mock private ProductRepository     productRepository;
    @Mock private BrandRepository       brandRepository;
    @Mock private SubcategoryRepository subcategoryRepository;
    @Mock private ProductMapper         productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private CustomUserPrincipal principal;
    private Brand               brand;
    private Product             product;
    private ProductRequestDTO   requestDTO;

    @BeforeEach
    void setUp() {
        principal  = mock(CustomUserPrincipal.class);
        when(principal.getId()).thenReturn(1L);

        brand = Brand.builder().name("SEB-WOLF").state(true).isDeleted(false).build();
        brand = setId(brand, 1L);

        product = Product.builder()
                .name("polo goku")
                .brand(brand)
                .state(true).isDeleted(false)
                .build();
        product = setId(product, 1L);

        requestDTO = ProductRequestDTO.builder()
                .name("polo goku")
                .brandId(1L)
                .subcategoryIds(Collections.emptyList())
                .build();
    }

    @Nested
    @DisplayName("createProduct")
    class CreateProduct {

        @Test
        @DisplayName("debe crear el producto correctamente")
        void shouldCreateProduct() {
            ProductDetailResponseDTO expected = ProductDetailResponseDTO.builder()
                    .id(1L).name("polo goku").brand("SEB-WOLF").build();

            when(productRepository.findByName(anyString())).thenReturn(Optional.empty());
            when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
            when(productMapper.toEntity(requestDTO)).thenReturn(product);
            when(productRepository.save(any(Product.class))).thenReturn(product);
            when(productMapper.toDetailResponseDTO(product)).thenReturn(expected);

            ProductDetailResponseDTO result = productService.createProduct(requestDTO, principal);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("polo goku");
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("debe lanzar DuplicateResourceException si el nombre ya existe")
        void shouldThrowWhenNameExists() {
            when(productRepository.findByName(anyString())).thenReturn(Optional.of(product));

            assertThatThrownBy(() -> productService.createProduct(requestDTO, principal))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("already exists");

            verify(productRepository, never()).save(any());
        }

        @Test
        @DisplayName("debe lanzar NotFoundException si la marca no existe")
        void shouldThrowWhenBrandNotFound() {
            when(productRepository.findByName(anyString())).thenReturn(Optional.empty());
            when(brandRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.createProduct(requestDTO, principal))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Brand");
        }
    }

    @Nested
    @DisplayName("getProductById")
    class GetProductById {

        @Test
        @DisplayName("debe retornar el producto si existe")
        void shouldReturnProduct() {
            ProductDetailResponseDTO dto = ProductDetailResponseDTO.builder().id(1L).build();
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(productMapper.toDetailResponseDTO(product)).thenReturn(dto);

            Optional<ProductDetailResponseDTO> result = productService.getProductById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("debe retornar empty si no existe")
        void shouldReturnEmptyWhenNotFound() {
            when(productRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<ProductDetailResponseDTO> result = productService.getProductById(99L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("deleteProduct")
    class DeleteProduct {

        @Test
        @DisplayName("debe hacer soft delete correctamente")
        void shouldSoftDelete() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(productRepository.save(any())).thenReturn(product);

            productService.deleteProduct(1L, principal);

            assertThat(product.getIsDeleted()).isTrue();
            verify(productRepository).save(product);
        }

        @Test
        @DisplayName("debe lanzar NotFoundException si no existe")
        void shouldThrowWhenNotFound() {
            when(productRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.deleteProduct(99L, principal))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    // Helper para setear ID en entidades con Lombok builder (no tienen setter de id por convención)
    @SuppressWarnings("unchecked")
    private <T> T setId(T entity, Long id) {
        try {
            var field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            // ignorar en tests
        }
        return entity;
    }
}