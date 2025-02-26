package com.sensedia.desafio.integrations.service.customer;

import com.sensedia.desafio.integrations.controller.CustomerController;
import com.sensedia.desafio.integrations.domain.Customer;
import com.sensedia.desafio.integrations.dto.request.CustomerRequestDTO;
import com.sensedia.desafio.integrations.dto.response.CustomerResponseDTO;
import com.sensedia.desafio.integrations.exception.NotFoundException;
import com.sensedia.desafio.integrations.exception.RequiredObjectIsNullException;
import com.sensedia.desafio.integrations.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerResponseDTO create(CustomerRequestDTO customerRequestDTO) {

        if (customerRequestDTO == null) {
            throw new RequiredObjectIsNullException();
        }

        Customer customer = Customer.builder()
                .name(customerRequestDTO.getName())
                .email(customerRequestDTO.getEmail())
                .cpf(customerRequestDTO.getCpf())
                .build();

        Customer saved = customerRepository.save(customer);
        CustomerResponseDTO dto = mapToDTO(saved);

        return dto.add(linkTo(methodOn(CustomerController.class)
                .getById(dto.getId()))
                .withSelfRel()
        );
    }

    @Override
    public List<CustomerResponseDTO> getAll() {
        List<CustomerResponseDTO> customerResponseDTOS = customerRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();

        customerResponseDTOS
                .forEach(p -> p.add(
                        linkTo(methodOn(CustomerController.class)
                                .getById(p.getId()))
                                .withSelfRel())
                );

        return customerResponseDTOS;
    }

    @Override
    public CustomerResponseDTO getById(Long id) {
        Customer Customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        return mapToDTO(Customer)
                .add(linkTo(methodOn(CustomerController.class)
                        .getById(id))
                        .withSelfRel()
                );
    }

    @Override
    @Transactional
    public CustomerResponseDTO update(Long id, CustomerRequestDTO customerRequest) {

        if (customerRequest == null) {
            throw new RequiredObjectIsNullException();
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setCpf(customerRequest.getCpf());

        return mapToDTO(customer)
                .add(linkTo(methodOn(CustomerController.class)
                        .getById(id))
                        .withSelfRel()
                );
    }

    @Override
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    private CustomerResponseDTO mapToDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .cpf(customer.getCpf())
                .build();
    }
}
