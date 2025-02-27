package com.sensedia.desafio.integrations.messaging;

import com.sensedia.desafio.integrations.domain.Order;
import com.sensedia.desafio.integrations.domain.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage {

    private Long orderId;
    private OrderStatusEnum status;
    private BigDecimal totalPrice;
    private String orderDate;
    private List<OrderItemMessage> items;

    public static OrderMessage fromOrder(Order order) {
        List<OrderItemMessage> itemMessages = order.getItems().stream()
                .map(item -> new OrderItemMessage(item.getProduct().getId(), item.getQuantity(), item.getPrice()))
                .toList();
        return new OrderMessage(
                order.getId(),
                order.getStatus(),
                order.getTotalPrice(),
                String.valueOf(order.getOrderDate()),
                itemMessages
        );
    }

}
