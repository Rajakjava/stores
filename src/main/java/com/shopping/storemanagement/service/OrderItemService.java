package com.shopping.storemanagement.service;

import com.shopping.storemanagement.entity.OrderDetails;
import com.shopping.storemanagement.entity.OrderItem;
import com.shopping.storemanagement.entity.Product;
import com.shopping.storemanagement.exception.InvalidOrderException;
import com.shopping.storemanagement.exception.InvalidOrderItemException;
import com.shopping.storemanagement.exception.InvalidProductException;
import com.shopping.storemanagement.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    public List<OrderItem> getAllItems() {
        return orderItemRepository.findAll();
    }

    public OrderItem addItem(OrderItem item) throws InvalidProductException, InvalidOrderException {
        Product product = productService.getProduct(item.getProductId()).orElseThrow();
        OrderDetails orderDetails = orderDetailsService.getOrder(item.getOrderId()).orElseThrow();

        return orderItemRepository.saveAndFlush(item);
    }

    public OrderItem getItem(int id) throws InvalidOrderItemException {
        return orderItemRepository.findById(id).orElseThrow(InvalidOrderItemException::new);
    }

    public void updateItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    public void deleteItem(int id) {
        orderItemRepository.deleteById(id);
    }
}
