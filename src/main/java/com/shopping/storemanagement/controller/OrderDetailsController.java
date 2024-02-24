package com.shopping.storemanagement.controller;

import com.shopping.storemanagement.entity.OrderDetails;
import com.shopping.storemanagement.entity.OrderItem;
import com.shopping.storemanagement.entity.Product;
import com.shopping.storemanagement.exception.InvalidOrderException;
import com.shopping.storemanagement.service.OrderDetailsService;
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

@RestController()
@RequestMapping("/api/orders")
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;
    private static Logger logger = LoggerFactory.getLogger(OrderDetailsController.class);


    @Operation(summary = "Gets All Orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of All Orders",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
    })
    @GetMapping("/")
    public ResponseEntity<?> getAllOrders() {
        List<OrderDetails> orderDetailsList = orderDetailsService.getAllOrders();

        if (orderDetailsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            {
                return ResponseEntity.ok(orderDetailsList);
            }
        }
    }

    @Operation(summary = "Gets an Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order with given id",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<?> getAllProducts(@PathVariable(name = "id") int id) {
        try {
            Optional<OrderDetails> orderDetail = orderDetailsService.getOrder(id);
            return ResponseEntity.ok(orderDetail.get());
        }
        catch (InvalidOrderException ex) {
            logger.error("Order Details with order id {} not found", id ,ex);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add a Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add a Order",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDetails.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<?> addOrder(@RequestBody OrderDetails orderDetails) {
        try {
            orderDetailsService.addOrder(orderDetails);
            return ResponseEntity.ok(orderDetails);
        } catch (InvalidOrderException e) {
            logger.error("Unable to add product {}", orderDetails, e);
            return ResponseEntity.badRequest().body("Invalid data supplied");
        }
    }

    @Operation(summary = "Update a Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable(name = "id") int id, @RequestBody OrderDetails orderDetails) {
        try {
            OrderDetails existingOrder = orderDetailsService.getOrder(id).get();
            existingOrder.setCustomerName(orderDetails.getCustomerName());
            existingOrder.setModifiedOn(LocalDateTime.now());
            existingOrder.setPaymentType(orderDetails.getPaymentType());
            orderDetailsService.updateOrder(existingOrder);
            return ResponseEntity.ok("Order updated successfully");
        }
        catch (InvalidOrderException ex) {
            logger.error("Order with order id {} not found", id ,ex);
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "delete an Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDetails.class))})})
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable(name = "id") int id) {
        orderDetailsService.deleteOrder(id);
    }
}
