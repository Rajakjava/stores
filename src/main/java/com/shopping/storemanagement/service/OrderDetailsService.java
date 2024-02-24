package com.shopping.storemanagement.service;

import com.shopping.storemanagement.entity.OrderDetails;
import com.shopping.storemanagement.exception.InvalidOrderException;
import com.shopping.storemanagement.repository.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<OrderDetails> getAllOrders() {
        return orderDetailsRepository.findAll();
    }

    public Optional<OrderDetails> getOrder(int id) throws InvalidOrderException {
        return Optional.ofNullable(orderDetailsRepository.findById(id).orElseThrow(
                InvalidOrderException::new));
    }

    public OrderDetails addOrder(OrderDetails orderDetails) throws InvalidOrderException {
        if (orderDetails.getOrderItems().isEmpty()) {
            throw new InvalidOrderException();
        } else {
            orderDetailsRepository.saveAndFlush(orderDetails);
        }

        return orderDetails;
    }

    public void updateOrder(OrderDetails orderDetails) {
        orderDetailsRepository.save(orderDetails);
    }

    public void deleteOrder(int id) {
        orderDetailsRepository.deleteById(id);
    }
 }
