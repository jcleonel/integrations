package com.sensedia.desafio.integrations.dto.response;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {

    private Long productId;
    private int quantity;
    private BigDecimal price;

}
