package com.sensedia.desafio.integrations.service.product;

import com.sensedia.desafio.integrations.controller.ProductController;
import com.sensedia.desafio.integrations.domain.Product;
import com.sensedia.desafio.integrations.dto.request.ProductRequestDTO;
import com.sensedia.desafio.integrations.dto.response.ProductResponseDTO;
import com.sensedia.desafio.integrations.exception.NotFoundException;
import com.sensedia.desafio.integrations.exception.RequiredObjectIsNullException;
import com.sensedia.desafio.integrations.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductResponseDTO create(ProductRequestDTO productRequest) {

        if (productRequest == null) {
            throw new RequiredObjectIsNullException();
        }

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .build();

        Product saved = productRepository.save(product);
        ProductResponseDTO dto = mapToDTO(saved);

        return dto.add(linkTo(methodOn(ProductController.class)
                .getById(dto.getId()))
                .withSelfRel()
        );
    }

    @Override
    public List<ProductResponseDTO> getAll() {
        List<ProductResponseDTO> productResponseDTOS = productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();

        productResponseDTOS
                .forEach(p -> p.add(
                        linkTo(methodOn(ProductController.class)
                                .getById(p.getId()))
                                .withSelfRel())
                );

        return productResponseDTOS;
    }

    @Override
    public ProductResponseDTO getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        return mapToDTO(product)
                .add(linkTo(methodOn(ProductController.class)
                        .getById(id))
                        .withSelfRel()
                );
    }

    @Override
    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequest) {

        if (productRequest == null) {
            throw new RequiredObjectIsNullException();
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());

        return mapToDTO(product)
                .add(linkTo(methodOn(ProductController.class)
                        .getById(id))
                        .withSelfRel()
                );
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.delete(product);
    }

    private ProductResponseDTO mapToDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
