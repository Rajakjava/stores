package com.shopping.storemanagement.controller;

import com.shopping.storemanagement.entity.Product;
import com.shopping.storemanagement.exception.InvalidProductException;
import com.shopping.storemanagement.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Operation(summary = "Add a Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add a Product",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try {
            productService.addProduct(product);
            return ResponseEntity.ok(product);
        } catch (InvalidProductException e) {
            logger.error("Unable to add product {}", product, e);
            return ResponseEntity.badRequest().body("Invalid data supplied");
        }
    }

    @Operation(summary = "Gets All products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of All products",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
    })
    @GetMapping("/")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            {
                return ResponseEntity.ok(products);
            }
        }
    }

    @Operation(summary = "Get a Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product with given id",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable(name = "id") int id) {

        try {
            Optional<Product> product = productService.getProduct(id);
            return ResponseEntity.ok(product.get());
        }
        catch (InvalidProductException ex) {
            logger.error("product with product id {} not found", id ,ex);
            return ResponseEntity.notFound().build();
            }
    }

    @Operation(summary = "Update a Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody Product product, @PathVariable(name = "id") int id) {
        try {
            Product existingProduct = productService.getProduct(id).get();
            existingProduct.setName(product.getName());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setDiscount(product.getDiscount());
            existingProduct.setModifiedOn(LocalDateTime.now());
            productService.updateProduct(product);
            return ResponseEntity.ok("Product updated successfully");
        }
        catch (InvalidProductException ex) {
            logger.error("product with product id {} not found", id ,ex);
            return ResponseEntity.notFound().build();
        }
    }
}
