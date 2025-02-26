package com.sensedia.desafio.integrations.mocks;

import com.sensedia.desafio.integrations.domain.Product;
import com.sensedia.desafio.integrations.dto.request.ProductRequestDTO;
import com.sensedia.desafio.integrations.dto.response.ProductResponseDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockProduct {

    public Product mockEntity(Integer number) {
        Product product = new Product();
        product.setId(number.longValue());
        product.setName("Name Product" + number);
        product.setDescription("Description Product" + number);
        product.setPrice(BigDecimal.valueOf(25));
        product.setStock(10);
        return product;
    }

    public ProductRequestDTO mockRequestDTO(Integer number) {
        ProductRequestDTO ProductRequestDTO = new ProductRequestDTO();
        ProductRequestDTO.setName("Name Product" + number);
        ProductRequestDTO.setDescription("Description Product" + number);
        ProductRequestDTO.setPrice(BigDecimal.valueOf(25));
        ProductRequestDTO.setStock(10);
        return ProductRequestDTO;
    }

    public ProductResponseDTO mockResponseDTO(Integer number) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(number.longValue());
        productResponseDTO.setName("Name Product" + number);
        productResponseDTO.setDescription("Description Product" + number);
        productResponseDTO.setPrice(BigDecimal.valueOf(25));
        productResponseDTO.setStock(10);
        return productResponseDTO;
    }

    public List<Product> mockEntityList() {
        List<Product> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }
}
