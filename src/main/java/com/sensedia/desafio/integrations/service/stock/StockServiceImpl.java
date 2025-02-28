package com.sensedia.desafio.integrations.service.stock;

import com.sensedia.desafio.integrations.domain.OrderItem;
import com.sensedia.desafio.integrations.domain.Product;
import com.sensedia.desafio.integrations.exception.InsufficientStockException;
import com.sensedia.desafio.integrations.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Override
    public void updateStock(Product product, int requestedQuantity) {


        product.setStock(product.getStock() - requestedQuantity);
        productRepository.save(product);
    }

    @Override
    public void validateStock(Product product, int requestedQuantity) {
        if (product.getStock() < requestedQuantity) {
            throw new InsufficientStockException("Insufficient stock for the product: " + product.getName());
        }
    }

    @Transactional
    @Override
    public void returnItemsToStock(List<OrderItem> items) {
        items.forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        });
    }
}
