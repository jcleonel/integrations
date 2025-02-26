package com.sensedia.desafio.integrations.service;

import com.sensedia.desafio.integrations.dto.request.ProductRequestDTO;
import com.sensedia.desafio.integrations.dto.response.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO create(ProductRequestDTO productRequest);
    List<ProductResponseDTO> getAll();
    ProductResponseDTO getById(Long id);
    ProductResponseDTO update(Long id, ProductRequestDTO productRequest);
    void delete(Long id);

}
