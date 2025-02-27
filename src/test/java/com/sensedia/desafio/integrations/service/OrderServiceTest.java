package com.sensedia.desafio.integrations.service;

import com.sensedia.desafio.integrations.domain.*;
import com.sensedia.desafio.integrations.dto.request.OrderItemRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderStatusUpdateRequestDTO;
import com.sensedia.desafio.integrations.dto.response.OrderResponseDTO;
import com.sensedia.desafio.integrations.exception.InsufficientStockException;
import com.sensedia.desafio.integrations.repository.CustomerRepository;
import com.sensedia.desafio.integrations.repository.OrderRepository;
import com.sensedia.desafio.integrations.repository.ProductRepository;
import com.sensedia.desafio.integrations.service.order.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    private Customer customer;
    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = Customer.builder().id(1L).name("Cliente Teste").build();
        product = Product.builder().id(1L).name("Produto Teste").price(BigDecimal.valueOf(100)).stock(10).build();
    }

    @Test
    public void testCreateOrderSuccess() {
        // Configurar mocks
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .customerId(1L)
                .items(List.of(OrderItemRequestDTO.builder().productId(1L).quantity(2).build()))
                .build();

        OrderResponseDTO order = orderService.createOrder(orderRequest);

        assertEquals(8, product.getStock());
        assertEquals(BigDecimal.valueOf(200), order.getTotalPrice());
        assertEquals(OrderStatusEnum.PENDING, order.getStatus());
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void testCreateOrderInsufficientStock() {
        product.setStock(1);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .customerId(1L)
                .items(List.of(OrderItemRequestDTO.builder().productId(1L).quantity(2).build()))
                .build();

        Exception exception = assertThrows(InsufficientStockException.class, () -> orderService.createOrder(orderRequest));
        assertTrue(exception.getMessage().contains("Insufficient stock"));
    }

    @Test
    public void testUpdateOrderStatusCancelPendingOrder() {

        Order order = Order.builder()
                .id(1L)
                .customer(customer)
                .status(OrderStatusEnum.PENDING)
                .totalPrice(BigDecimal.valueOf(200))
                .orderDate(java.time.LocalDateTime.now())
                .build();
        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(2)
                .price(BigDecimal.valueOf(200))
                .build();
        order.setItems(List.of(orderItem));
        product.setStock(8);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderStatusUpdateRequestDTO statusUpdate = OrderStatusUpdateRequestDTO.builder()
                .status(OrderStatusEnum.CANCELED)
                .build();

        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(1L, statusUpdate);

        assertEquals(OrderStatusEnum.CANCELED, updatedOrder.getStatus());
        assertEquals(10, product.getStock());
    }

    @Test
    public void testUpdateOrderStatusCancelNonPendingOrder() {
        Order order = Order.builder()
                .id(1L)
                .customer(customer)
                .status(OrderStatusEnum.PAID)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderStatusUpdateRequestDTO statusUpdate = OrderStatusUpdateRequestDTO.builder()
                .status(OrderStatusEnum.CANCELED)
                .build();

        Exception exception = assertThrows(UnsupportedOperationException.class, () -> orderService.updateOrderStatus(1L, statusUpdate));
        assertTrue(exception.getMessage().contains("pending"));
    }

}
