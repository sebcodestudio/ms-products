package com.sebcode.msproducts.order.mapper;

import com.sebcode.msproducts.order.dto.response.OrderItemResponseDTO;
import com.sebcode.msproducts.order.dto.response.OrderResponseDTO;
import com.sebcode.msproducts.order.entity.Order;
import com.sebcode.msproducts.order.entity.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDTO toResponseDTO(Order order);

    OrderItemResponseDTO toItemResponseDTO(OrderItem item);

    List<OrderItemResponseDTO> toItemResponseDTOs(List<OrderItem> items);

}
