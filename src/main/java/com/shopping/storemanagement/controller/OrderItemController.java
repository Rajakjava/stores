package com.shopping.storemanagement.controller;

import com.shopping.storemanagement.entity.OrderItem;
import com.shopping.storemanagement.entity.Product;
import com.shopping.storemanagement.exception.InvalidOrderException;
import com.shopping.storemanagement.exception.InvalidOrderItemException;
import com.shopping.storemanagement.exception.InvalidProductException;
import com.shopping.storemanagement.service.OrderItemService;
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
@RequestMapping("/api/orderitem")
public class OrderItemController {

    private static Logger logger = LoggerFactory.getLogger(OrderItemController.class);

    @Autowired
    private OrderItemService orderItemService;

    @Operation(summary = "Add a OrderItem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order Item Added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItem.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<?> addProduct(@RequestBody OrderItem orderItem) {
        try {
            orderItemService.addItem(orderItem);
            return ResponseEntity.ok(orderItem);
        } catch (InvalidOrderException | InvalidProductException e) {
            logger.error("Unable to add product {}", orderItem, e);
            return ResponseEntity.badRequest().body("Invalid data supplied");
        }
    }

    @Operation(summary = "Gets All OrderItem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of All Order Items",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
    })
    @GetMapping("/")
    public ResponseEntity<?> getAllProducts() {
        List<OrderItem> orderItems = orderItemService.getAllItems();

        if (orderItems.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            {
                return ResponseEntity.ok(orderItems);
            }
        }
    }

    @Operation(summary = "Get an Order Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product with given id",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable(name = "id") int id) {

        try {
            OrderItem orderItem = orderItemService.getItem(id);
            return ResponseEntity.ok(orderItem);
        }
        catch (InvalidOrderItemException ex) {
            logger.error("product with product id {} not found", id ,ex);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update an order Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Order item not found",
                    content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@RequestBody OrderItem orderItem, @PathVariable(name = "id") int id) {
        try {
            OrderItem item = orderItemService.getItem(id);
            item.setQuantity(orderItem.getQuantity());
            item.setProductId(orderItem.getProductId());
            item.setOrderId(orderItem.getOrderId());
            item.setModifiedOn(LocalDateTime.now());
            orderItemService.updateItem(item);
            return ResponseEntity.ok("Order item updated successfully");
        }
        catch (InvalidOrderItemException ex) {
            logger.error("Order Item with id {} not found", id ,ex);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "delete an order Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))})})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable(name = "id") int id) {
        orderItemService.deleteItem(id);
        return ResponseEntity.ok("Order item deleted successfully");
    }

}
