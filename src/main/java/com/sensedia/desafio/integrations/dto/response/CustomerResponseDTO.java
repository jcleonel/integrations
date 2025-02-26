package com.sensedia.desafio.integrations.dto.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO extends RepresentationModel<CustomerResponseDTO> {

    private Long id;
    private String name;
    private String email;
    private String cpf;

}
