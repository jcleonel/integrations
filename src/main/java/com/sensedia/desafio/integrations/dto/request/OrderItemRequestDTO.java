package com.sensedia.desafio.integrations.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequestDTO {

    private Long productId;
    private int quantity;

}
