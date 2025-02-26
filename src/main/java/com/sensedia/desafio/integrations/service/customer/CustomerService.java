package com.sensedia.desafio.integrations.service.customer;

import com.sensedia.desafio.integrations.dto.request.CustomerRequestDTO;
import com.sensedia.desafio.integrations.dto.response.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {

    CustomerResponseDTO create(CustomerRequestDTO customerRequest);
    List<CustomerResponseDTO> getAll();
    CustomerResponseDTO getById(Long id);
    CustomerResponseDTO update(Long id, CustomerRequestDTO customerRequest);
    void delete(Long id);

}
