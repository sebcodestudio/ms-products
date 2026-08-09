package com.sebcode.msproducts.order.service;

import com.sebcode.msproducts.order.dto.request.OrderRequestDTO;
import com.sebcode.msproducts.order.dto.request.PayOrderRequestDTO;
import com.sebcode.msproducts.order.dto.response.OrderResponseDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IOrderService {

    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);

    OrderResponseDTO payOrder(UUID publicReference, PayOrderRequestDTO requestDTO);

    OrderResponseDTO getOrderStatus(UUID publicReference);

    Page<OrderResponseDTO> searchOrders(int page, int size);

}
