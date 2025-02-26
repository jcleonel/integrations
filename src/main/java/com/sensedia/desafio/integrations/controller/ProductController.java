package com.sensedia.desafio.integrations.controller;

import com.sensedia.desafio.integrations.api.ProductApi;
import com.sensedia.desafio.integrations.dto.request.ProductRequestDTO;
import com.sensedia.desafio.integrations.dto.response.ProductResponseDTO;
import com.sensedia.desafio.integrations.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController implements ProductApi {

    @Autowired
    private ProductService productService;

    public ResponseEntity<ProductResponseDTO> create(ProductRequestDTO productRequest) {
        ProductResponseDTO product = productService.create(productRequest);
        return ResponseEntity.ok(product);
    }

    public ResponseEntity<List<ProductResponseDTO>> getAll() {
        List<ProductResponseDTO> products = productService.getAll();
        return ResponseEntity.ok(products);
    }

    public ResponseEntity<ProductResponseDTO> getById(Long id) {
        ProductResponseDTO product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    public ResponseEntity<ProductResponseDTO> update(Long id, ProductRequestDTO productRequest) {
        ProductResponseDTO updatedProduct = productService.update(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    public ResponseEntity<Void> delete(Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
