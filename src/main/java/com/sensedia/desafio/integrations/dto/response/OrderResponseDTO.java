package com.sensedia.desafio.integrations.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.sensedia.desafio.integrations.domain.OrderStatusEnum;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO extends RepresentationModel<OrderResponseDTO> {

    private Long id;
    private Long customerId;
    private List<OrderItemResponseDTO> items;
    private OrderStatusEnum status;
    private BigDecimal totalPrice;
    private String orderDate;

}
