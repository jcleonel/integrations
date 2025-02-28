package com.sensedia.desafio.integrations.service.order;

import com.sensedia.desafio.integrations.controller.OrderController;
import com.sensedia.desafio.integrations.domain.*;
import com.sensedia.desafio.integrations.dto.request.OrderItemRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderStatusUpdateRequestDTO;
import com.sensedia.desafio.integrations.dto.response.OrderItemResponseDTO;
import com.sensedia.desafio.integrations.dto.response.OrderResponseDTO;
import com.sensedia.desafio.integrations.exception.NotFoundException;
import com.sensedia.desafio.integrations.messaging.OrderMessagePublisher;
import com.sensedia.desafio.integrations.repository.CustomerRepository;
import com.sensedia.desafio.integrations.repository.OrderRepository;
import com.sensedia.desafio.integrations.repository.ProductRepository;
import com.sensedia.desafio.integrations.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private OrderMessagePublisher orderMessagePublisher;
    @Autowired
    private StockService stockService;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        Customer customer = getCustomerById(orderRequest.getCustomerId());
        Order order = initializeOrder(customer);
        
        processOrderItems(order, orderRequest.getItems());
        
        Order savedOrder = orderRepository.save(order);
        return mapToDTO(savedOrder);
    }

    private Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
    }

    private Order initializeOrder(Customer customer) {
        return Order.builder()
                .customer(customer)
                .status(OrderStatusEnum.PENDING)
                .orderDate(LocalDateTime.now())
                .build();
    }

    private void processOrderItems(Order order, List<OrderItemRequestDTO> itemRequests) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemRequest : itemRequests) {
            Product product = getProductById(itemRequest.getProductId());
            stockService.validateStock(product, itemRequest.getQuantity());
            stockService.updateStock(product, itemRequest.getQuantity());
            
            OrderItem orderItem = createOrderItem(order, product, itemRequest.getQuantity());
            order.getItems().add(orderItem);
            
            totalPrice = totalPrice.add(orderItem.getPrice());
        }

        order.setTotalPrice(totalPrice);
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: ID " + productId));
    }

    private OrderItem createOrderItem(Order order, Product product, int quantity) {
        BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .price(itemTotal)
                .build();
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = findOrderById(id);
        return mapToDTO(order);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: ID " + id));
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long id) {
        return updateOrderStatus(id, new OrderStatusUpdateRequestDTO(OrderStatusEnum.CANCELED));
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatusUpdateRequestDTO statusRequest) {
        Order order = findOrderById(id);
        OrderStatusEnum newStatus = statusRequest.getStatus();

        validateStatusTransition(order, newStatus);
        
        if (newStatus == OrderStatusEnum.CANCELED) {
            stockService.returnItemsToStock(order.getItems());
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
        orderMessagePublisher.publishOrderStatusChange(order);

        return mapToDTO(order);
    }

    private void validateStatusTransition(Order order, OrderStatusEnum newStatus) {
        if (order.getStatus() == OrderStatusEnum.CANCELED) {
            throw new UnsupportedOperationException("Order " + order.getId() + " has a canceled status");
        }

        if (newStatus == OrderStatusEnum.CANCELED && order.getStatus() != OrderStatusEnum.PENDING) {
            throw new UnsupportedOperationException("Only pending orders can be canceled");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(this::mapToDTO);
    }

    private OrderResponseDTO mapToDTO(Order order) {
        Long id = order.getId();

        OrderResponseDTO responseDTO = OrderResponseDTO.builder()
                .id(id)
                .customerId(order.getCustomer().getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .orderDate(String.valueOf(order.getOrderDate()))
                .items(mapOrderItems(order.getItems()))
                .build();

        return responseDTO.add(linkTo(methodOn(OrderController.class)
                .getById(id))
                .withSelfRel()
        );
    }

    private List<OrderItemResponseDTO> mapOrderItems(List<OrderItem> items) {
        return items.stream()
                .map(item -> OrderItemResponseDTO.builder()
                        .productId(item.getProduct().getId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();
    }
}
