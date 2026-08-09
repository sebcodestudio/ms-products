package com.sebcode.msproducts.order.controller;

import com.sebcode.msproducts.common.response.PageResponse;
import com.sebcode.msproducts.order.dto.request.OrderRequestDTO;
import com.sebcode.msproducts.order.dto.request.PayOrderRequestDTO;
import com.sebcode.msproducts.order.dto.response.OrderResponseDTO;
import com.sebcode.msproducts.order.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Pedidos de checkout con pasarela de pago")
public class OrderController {

    private final IOrderService orderService;

    @PostMapping
    @Operation(summary = "Crear un pedido", description = "Checkout de invitado — valida stock y recalcula el total en el servidor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado en estado PENDING_PAYMENT"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o sin stock suficiente"),
            @ApiResponse(responseCode = "404", description = "Alguna variante del pedido no existe")
    })
    public ResponseEntity<OrderResponseDTO> create(@Valid @RequestBody OrderRequestDTO requestDTO) {
        log.info("Nuevo pedido de {}", requestDTO.getCustomerName());
        OrderResponseDTO created = orderService.createOrder(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{publicReference}/pay")
    @Operation(summary = "Pagar un pedido", description = "Cobra el token de Culqi generado en el checkout del cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado del intento de pago (PAID o PAYMENT_FAILED)"),
            @ApiResponse(responseCode = "400", description = "El pedido ya fue procesado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<OrderResponseDTO> pay(
            @PathVariable UUID publicReference,
            @Valid @RequestBody PayOrderRequestDTO requestDTO) {

        OrderResponseDTO result = orderService.payOrder(publicReference, requestDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{publicReference}")
    @Operation(summary = "Consultar estado de un pedido", description = "Usado por la página de confirmación del checkout.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<OrderResponseDTO> getByPublicReference(@PathVariable UUID publicReference) {
        return ResponseEntity.ok(orderService.getOrderStatus(publicReference));
    }

    @GetMapping("/admin/search")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar pedidos", description = "Solo accesible por administradores.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    public ResponseEntity<PageResponse<OrderResponseDTO>> search(
            @Parameter(description = "Número de página (0-based)")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be at least 0")
            int page,

            @Parameter(description = "Tamaño de página (máx 100)")
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size must be at most 100")
            int size) {

        Page<OrderResponseDTO> orders = orderService.searchOrders(page, size);
        return ResponseEntity.ok(PageResponse.of(orders));
    }

}
