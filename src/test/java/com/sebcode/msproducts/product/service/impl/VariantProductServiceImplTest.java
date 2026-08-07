package com.sebcode.msproducts.product.service.impl;

import com.sebcode.msproducts.exception.DuplicateResourceException;
import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.product.dto.request.VariantProductRequestDTO;
import com.sebcode.msproducts.product.dto.response.admin.VariantProductDetailAdminResponseDTO;
import com.sebcode.msproducts.product.entity.VariantProduct;
import com.sebcode.msproducts.product.mapper.VariantProductMapper;
import com.sebcode.msproducts.product.repository.VariantProductRepository;
import com.sebcode.msproducts.product.service.impl.VariantProductServiceImpl;
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

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("VariantProductServiceImpl")
class VariantProductServiceImplTest {

    @Mock private VariantProductRepository variantProductRepository;
    @Mock private VariantProductMapper     variantProductMapper;

    @InjectMocks
    private VariantProductServiceImpl variantProductService;

    private CustomUserPrincipal         principal;
    private VariantProductRequestDTO    requestDTO;
    private VariantProduct              variant;

    @BeforeEach
    void setUp() {
        principal = mock(CustomUserPrincipal.class);
        when(principal.getId()).thenReturn(1L);

        requestDTO = new VariantProductRequestDTO();
        requestDTO.setSku("polo-goku-black-m");
        requestDTO.setPrice(new BigDecimal("49.90"));
        requestDTO.setStock(50);

        variant = VariantProduct.builder()
                .sku("polo-goku-black-m")
                .price(new BigDecimal("49.90"))
                .stock(50).soldCount(0)
                .state(true).isDeleted(false)
                .build();
    }

    @Nested
    @DisplayName("createVariantProduct")
    class CreateVariantProduct {

        @Test
        @DisplayName("debe crear la variante correctamente cuando el SKU no existe")
        void shouldCreateVariant() {
            VariantProductDetailAdminResponseDTO expected =
                    VariantProductDetailAdminResponseDTO.builder().sku("polo-goku-black-m").build();

            when(variantProductRepository.findBySku("polo-goku-black-m")).thenReturn(Optional.empty());
            when(variantProductMapper.toEntity(requestDTO)).thenReturn(variant);
            when(variantProductRepository.save(variant)).thenReturn(variant);
            when(variantProductMapper.toDetailAdminResponseDTO(variant)).thenReturn(expected);

            VariantProductDetailAdminResponseDTO result =
                    variantProductService.createVariantProduct(requestDTO, principal);

            assertThat(result).isNotNull();
            assertThat(result.getSku()).isEqualTo("polo-goku-black-m");
            verify(variantProductRepository).save(variant);
        }

        @Test
        @DisplayName("debe lanzar DuplicateResourceException si el SKU ya existe y no está eliminado")
        void shouldThrowWhenSkuExists() {
            when(variantProductRepository.findBySku("polo-goku-black-m"))
                    .thenReturn(Optional.of(variant));

            assertThatThrownBy(() -> variantProductService.createVariantProduct(requestDTO, principal))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("polo-goku-black-m");

            verify(variantProductRepository, never()).save(any());
        }

        @Test
        @DisplayName("debe restaurar la variante si el SKU existe pero estaba eliminado")
        void shouldRestoreWhenSkuExistsButDeleted() {
            variant.setIsDeleted(true);
            VariantProductDetailAdminResponseDTO expected =
                    VariantProductDetailAdminResponseDTO.builder().sku("polo-goku-black-m").build();

            when(variantProductRepository.findBySku("polo-goku-black-m"))
                    .thenReturn(Optional.of(variant));
            when(variantProductRepository.save(variant)).thenReturn(variant);
            when(variantProductMapper.toDetailAdminResponseDTO(variant)).thenReturn(expected);

            VariantProductDetailAdminResponseDTO result =
                    variantProductService.createVariantProduct(requestDTO, principal);

            assertThat(result).isNotNull();
            assertThat(variant.getIsDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("deleteVariantProduct")
    class DeleteVariantProduct {

        @Test
        @DisplayName("debe hacer soft delete correctamente")
        void shouldSoftDelete() {
            when(variantProductRepository.findById(1L)).thenReturn(Optional.of(variant));
            when(variantProductRepository.save(any())).thenReturn(variant);

            variantProductService.deleteVariantProduct(1L, principal);

            assertThat(variant.getIsDeleted()).isTrue();
            assertThat(variant.getDeleteAt()).isNotNull();
            verify(variantProductRepository).save(variant);
        }

        @Test
        @DisplayName("debe lanzar NotFoundException si no existe")
        void shouldThrowWhenNotFound() {
            when(variantProductRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> variantProductService.deleteVariantProduct(99L, principal))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}