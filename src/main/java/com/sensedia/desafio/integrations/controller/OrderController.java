package com.sensedia.desafio.integrations.controller;

import com.sensedia.desafio.integrations.api.OrderApi;
import com.sensedia.desafio.integrations.dto.request.OrderRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderStatusUpdateRequestDTO;
import com.sensedia.desafio.integrations.dto.response.OrderResponseDTO;
import com.sensedia.desafio.integrations.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController implements OrderApi {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(OrderRequestDTO orderRequest) {
        OrderResponseDTO order = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(order);
    }

    @Override
    public ResponseEntity<Page<OrderResponseDTO>> getAll(int page, int size, String sort, String direction ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction.toUpperCase()), sort);
        Page<OrderResponseDTO> orderResponseDTOS = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orderResponseDTOS);
    }

    public ResponseEntity<OrderResponseDTO> getById(Long id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<OrderResponseDTO> cancelOrder(Long id) {
        OrderResponseDTO order = orderService.cancelOrder(id);
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<OrderResponseDTO> updateOrderStatus(Long id, OrderStatusUpdateRequestDTO statusRequest) {
        OrderResponseDTO order = orderService.updateOrderStatus(id, statusRequest);
        return ResponseEntity.ok(order);
    }

}
