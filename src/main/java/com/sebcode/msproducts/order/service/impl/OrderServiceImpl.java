package com.sebcode.msproducts.order.service.impl;

import com.sebcode.msproducts.exception.BadRequestException;
import com.sebcode.msproducts.exception.NotFoundException;
import com.sebcode.msproducts.order.dto.request.OrderItemRequestDTO;
import com.sebcode.msproducts.order.dto.request.OrderRequestDTO;
import com.sebcode.msproducts.order.dto.request.PayOrderRequestDTO;
import com.sebcode.msproducts.order.dto.response.OrderResponseDTO;
import com.sebcode.msproducts.order.entity.Order;
import com.sebcode.msproducts.order.entity.OrderItem;
import com.sebcode.msproducts.order.entity.OrderStatus;
import com.sebcode.msproducts.order.mapper.OrderMapper;
import com.sebcode.msproducts.order.payment.ChargeResult;
import com.sebcode.msproducts.order.payment.PaymentProvider;
import com.sebcode.msproducts.order.repository.OrderRepository;
import com.sebcode.msproducts.order.service.IOrderService;
import com.sebcode.msproducts.product.entity.VariantProduct;
import com.sebcode.msproducts.product.repository.VariantProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final VariantProductRepository variantProductRepository;
    private final OrderMapper orderMapper;
    private final PaymentProvider paymentProvider;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDto : requestDTO.getItems()) {
            VariantProduct variant = variantProductRepository.findById(itemDto.getVariantProductId())
                    .orElseThrow(() -> new NotFoundException(
                            "Variante con ID " + itemDto.getVariantProductId() + " no encontrada"));

            if (!variant.isAvailable()) {
                throw new BadRequestException(
                        "'" + variant.getProduct().getName() + "' ya no esta disponible");
            }
            if (variant.getStock() < itemDto.getQuantity()) {
                throw new BadRequestException(
                        "Stock insuficiente para '" + variant.getProduct().getName() + "'");
            }

            // Precio y nombre se "fotografian" acá — si cambian después, el
            // pedido histórico no debe moverse.
            BigDecimal unitPrice = variant.getFinalPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            total = total.add(lineTotal);

            String variantLabel = variant.getAttributesMap().entrySet().stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining(", "));

            items.add(OrderItem.builder()
                    .variantProduct(variant)
                    .productName(variant.getProduct().getName())
                    .variantLabel(variantLabel)
                    .unitPrice(unitPrice)
                    .quantity(itemDto.getQuantity())
                    .lineTotal(lineTotal)
                    .state(true).isDeleted(false)
                    .build());
        }

        Order order = Order.builder()
                .customerName(requestDTO.getCustomerName())
                .customerPhone(requestDTO.getCustomerPhone())
                .customerEmail(requestDTO.getCustomerEmail())
                .customerAddress(requestDTO.getCustomerAddress())
                .subtotal(total)
                .total(total)
                .currency("PEN")
                .status(OrderStatus.PENDING_PAYMENT)
                .state(true).isDeleted(false)
                .build();
        items.forEach(order::addItem);

        Order saved = orderRepository.save(order);
        log.info("Pedido {} creado por {}, total {} {}",
                saved.getPublicReference(), saved.getCustomerName(), saved.getTotal(), saved.getCurrency());
        return orderMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public OrderResponseDTO payOrder(UUID publicReference, PayOrderRequestDTO requestDTO) {
        Order order = orderRepository.findByPublicReference(publicReference)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BadRequestException(
                    "Este pedido ya fue procesado (estado actual: " + order.getStatus() + ")");
        }

        ChargeResult result = paymentProvider.charge(
                order.getTotal(),
                order.getCurrency(),
                requestDTO.getCulqiToken(),
                "Pedido SEB-WOLF " + order.getPublicReference(),
                order.getCustomerEmail());

        if (result.success()) {
            order.setStatus(OrderStatus.PAID);
            order.setPaymentProvider(paymentProvider.getType());
            order.setPaymentReference(result.paymentReference());
            order.setPaidAt(LocalDateTime.now());
            applyStockForPaidOrder(order);
        } else {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            order.setPaymentFailureReason(result.failureReason());
        }

        Order saved = orderRepository.save(order);
        log.info("Pago de pedido {} -> {}", saved.getPublicReference(), saved.getStatus());
        return orderMapper.toResponseDTO(saved);
    }

    /**
     * El cobro ya fue exitoso en este punto (Culqi ya tomó el dinero). Si el
     * stock cambió entre crear el pedido y pagarlo (venta simultánea del
     * último ítem), el pedido queda igual como PAID — la plata ya se cobró,
     * hay una obligación real — pero se deja un log ERROR bien visible para
     * seguimiento manual. Automatizar el reembolso por sobreventa queda
     * fuera de este alcance inicial.
     */
    private void applyStockForPaidOrder(Order order) {
        for (OrderItem item : order.getItems()) {
            try {
                item.getVariantProduct().decreaseStock(item.getQuantity());
                variantProductRepository.save(item.getVariantProduct());
            } catch (IllegalStateException e) {
                log.error("SOBREVENTA tras cobro exitoso — pedido {} item variante {}: {}",
                        order.getPublicReference(), item.getVariantProduct().getId(), e.getMessage());
            }
        }
    }

    @Override
    public OrderResponseDTO getOrderStatus(UUID publicReference) {
        Order order = orderRepository.findByPublicReference(publicReference)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado"));
        return orderMapper.toResponseDTO(order);
    }

    @Override
    public Page<OrderResponseDTO> searchOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return orderRepository.findAllList(pageable).map(orderMapper::toResponseDTO);
    }

}
