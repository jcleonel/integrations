package com.sensedia.desafio.integrations.service.order;

import com.sensedia.desafio.integrations.dto.request.OrderRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderStatusUpdateRequestDTO;
import com.sensedia.desafio.integrations.dto.response.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO orderRequest);
    OrderResponseDTO getOrderById(Long id);
    OrderResponseDTO cancelOrder(Long id);
    OrderResponseDTO updateOrderStatus(Long id, OrderStatusUpdateRequestDTO statusRequest);
    Page<OrderResponseDTO> getAllOrders(Pageable pageable);

}
