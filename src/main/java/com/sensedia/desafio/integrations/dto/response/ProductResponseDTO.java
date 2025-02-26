package com.sensedia.desafio.integrations.dto.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO extends RepresentationModel<ProductResponseDTO> {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

}
