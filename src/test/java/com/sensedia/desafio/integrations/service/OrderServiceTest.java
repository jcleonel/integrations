package com.sensedia.desafio.integrations.service;

import com.sensedia.desafio.integrations.domain.*;
import com.sensedia.desafio.integrations.dto.request.OrderItemRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderRequestDTO;
import com.sensedia.desafio.integrations.dto.request.OrderStatusUpdateRequestDTO;
import com.sensedia.desafio.integrations.dto.response.OrderResponseDTO;
import com.sensedia.desafio.integrations.exception.InsufficientStockException;
import com.sensedia.desafio.integrations.messaging.OrderMessagePublisher;
import com.sensedia.desafio.integrations.repository.CustomerRepository;
import com.sensedia.desafio.integrations.repository.OrderRepository;
import com.sensedia.desafio.integrations.repository.ProductRepository;
import com.sensedia.desafio.integrations.service.order.OrderServiceImpl;
import com.sensedia.desafio.integrations.service.stock.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockService stockService;

    @Mock
    private OrderMessagePublisher orderMessagePublisher;

    @InjectMocks
    private OrderServiceImpl orderService;

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
        doNothing().when(stockService).validateStock(any(Product.class), anyInt());
        doNothing().when(stockService).updateStock(any(Product.class), anyInt());

        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .customerId(1L)
                .items(List.of(OrderItemRequestDTO.builder().productId(1L).quantity(2).build()))
                .build();

        OrderResponseDTO order = orderService.createOrder(orderRequest);

        assertEquals(BigDecimal.valueOf(200), order.getTotalPrice());
        assertEquals(OrderStatusEnum.PENDING, order.getStatus());
        assertEquals(1, order.getItems().size());
        verify(stockService).validateStock(any(Product.class), eq(2));
        verify(stockService).updateStock(any(Product.class), eq(2));
    }

    @Test
    public void testCreateOrderInsufficientStock() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doThrow(new InsufficientStockException("Insufficient stock"))
                .when(stockService).validateStock(any(Product.class), anyInt());

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

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(stockService).returnItemsToStock(anyList());
        doNothing().when(orderMessagePublisher).publishOrderStatusChange(any(Order.class));

        OrderStatusUpdateRequestDTO statusUpdate = OrderStatusUpdateRequestDTO.builder()
                .status(OrderStatusEnum.CANCELED)
                .build();

        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(1L, statusUpdate);

        assertEquals(OrderStatusEnum.CANCELED, updatedOrder.getStatus());
        verify(stockService).returnItemsToStock(order.getItems());
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
