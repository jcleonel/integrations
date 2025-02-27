package com.sensedia.desafio.integrations.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemMessage {

    private Long productId;
    private int quantity;
    private BigDecimal price;

}
