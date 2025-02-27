package com.sensedia.desafio.integrations.dto.request;

import com.sensedia.desafio.integrations.domain.OrderStatusEnum;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdateRequestDTO {

    private OrderStatusEnum status;

}
