package com.sensedia.desafio.integrations.service.order;

import com.sensedia.desafio.integrations.controller.OrderController;
import com.sensedia.desafio.integrations.domain.*;
import com.sensedia.desafio.integrations.dto.request.OrderItemRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderStatusUpdateRequestDTO;
import com.sensedia.desafio.integrations.dto.response.OrderItemResponseDTO;
import com.sensedia.desafio.integrations.dto.response.OrderResponseDTO;
import com.sensedia.desafio.integrations.exception.InsufficientStockException;
import com.sensedia.desafio.integrations.exception.NotFoundException;
import com.sensedia.desafio.integrations.repository.CustomerRepository;
import com.sensedia.desafio.integrations.repository.OrderRepository;
import com.sensedia.desafio.integrations.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {

        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatusEnum.PENDING)
                .orderDate(LocalDateTime.now())
                .build();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemRequest : orderRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found: ID " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for the product: " + product.getName());
            }

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(itemTotal)
                    .build();

            order.getItems().add(orderItem);
            totalPrice = totalPrice.add(itemTotal);
        }

        order.setTotalPrice(totalPrice);
        Order saved = orderRepository.save(order);
        return mapToDTO(saved);

    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: ID " + id));

        return mapToDTO(order);
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long id) {
        return updateOrderStatus(id, new OrderStatusUpdateRequestDTO(OrderStatusEnum.CANCELED));
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatusUpdateRequestDTO statusRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: ID " + id));

        OrderStatusEnum newStatus = statusRequest.getStatus();

        if (order.getStatus() == OrderStatusEnum.CANCELED) {
            throw new UnsupportedOperationException("Order " + id + " has a canceled status");
        }

        if (newStatus == OrderStatusEnum.CANCELED && order.getStatus() != OrderStatusEnum.PENDING) {
            throw new UnsupportedOperationException("Only pending orders can be canceled");
        }

        if (newStatus == OrderStatusEnum.CANCELED) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
        return mapToDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(this::mapToDTO);
    }

    private OrderResponseDTO mapToDTO(Order order) {
        Long id = order.getId();

        OrderResponseDTO responseDTO = OrderResponseDTO.builder()
                .id(id)
                .customerId(order.getCustomer().getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .orderDate(String.valueOf(order.getOrderDate()))
                .items(order.getItems().stream().map(item ->
                        OrderItemResponseDTO.builder()
                                .productId(item.getProduct().getId())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build()
                ).toList())
                .build();

        return responseDTO.add(linkTo(methodOn(OrderController.class)
                .getById(id))
                .withSelfRel()
        );
    }
}