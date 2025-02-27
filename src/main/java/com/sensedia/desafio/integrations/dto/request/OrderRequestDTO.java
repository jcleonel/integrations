package com.sensedia.desafio.integrations.dto.request;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    private Long customerId;
    private List<OrderItemRequestDTO> items;

}
