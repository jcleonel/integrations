package com.sensedia.desafio.integrations.controller;

import com.sensedia.desafio.integrations.api.CustomerApi;
import com.sensedia.desafio.integrations.dto.request.CustomerRequestDTO;
import com.sensedia.desafio.integrations.dto.response.CustomerResponseDTO;
import com.sensedia.desafio.integrations.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController implements CustomerApi {

    @Autowired
    private CustomerService customerService;

    public ResponseEntity<CustomerResponseDTO> create(CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO customer = customerService.create(customerRequestDTO);
        return ResponseEntity.ok(customer);
    }

    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        List<CustomerResponseDTO> customers = customerService.getAll();
        return ResponseEntity.ok(customers);
    }

    public ResponseEntity<CustomerResponseDTO> getById(Long id) {
        CustomerResponseDTO customer = customerService.getById(id);
        return ResponseEntity.ok(customer);
    }

    public ResponseEntity<CustomerResponseDTO> update(Long id, CustomerRequestDTO customerRequest) {
        CustomerResponseDTO updatedCustomer = customerService.update(id, customerRequest);
        return ResponseEntity.ok(updatedCustomer);
    }

    public ResponseEntity<Void> delete(Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
