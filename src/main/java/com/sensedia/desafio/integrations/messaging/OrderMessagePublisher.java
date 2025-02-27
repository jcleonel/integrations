package com.sensedia.desafio.integrations.messaging;

import com.sensedia.desafio.integrations.domain.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderMessagePublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishOrderStatusChange(Order order) {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();

        OrderMessage message = OrderMessage.fromOrder(order);

        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.convertAndSend("status.order", message);
    }

}
