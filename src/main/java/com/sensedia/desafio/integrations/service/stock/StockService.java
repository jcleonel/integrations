package com.sensedia.desafio.integrations.service.stock;

import com.sensedia.desafio.integrations.domain.OrderItem;
import com.sensedia.desafio.integrations.domain.Product;

import java.util.List;

public interface StockService {

    void updateStock(Product product, int requestedQuantity);
    void validateStock(Product product, int requestedQuantity);
    void returnItemsToStock(List<OrderItem> items);

}
