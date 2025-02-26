package com.sensedia.desafio.integrations.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
}
