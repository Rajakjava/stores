package com.shopping.storemanagement.service;

import com.shopping.storemanagement.exception.InvalidProductException;
import com.shopping.storemanagement.repository.ProductRepository;
import com.shopping.storemanagement.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    public void addProduct(Product product) throws InvalidProductException {
        if (product.getName().isBlank() || product.getCategory().isBlank())
        {
            throw new InvalidProductException();
        }

        productRepository.saveAndFlush(product);

    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(int id) throws InvalidProductException {
        return Optional.ofNullable(productRepository.findById(id).orElseThrow(InvalidProductException::new));
    }

    public void updateProduct(Product product) throws InvalidProductException {
        productRepository.save(product);
    }
}
