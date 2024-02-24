package com.shopping.storemanagement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.storemanagement.entity.OrderDetails;
import com.shopping.storemanagement.entity.PaymentType;
import com.shopping.storemanagement.exception.InvalidOrderException;
import com.shopping.storemanagement.service.OrderDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderDetailsController.class)
class OrderDetailsControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderDetailsService orderDetailsService;



    @Test
    void testGetOrderDetails() throws Exception {
        List<OrderDetails> orders = new ArrayList<>();
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setId(1);
        orderDetails.setPaymentType(PaymentType.CARD);
        orderDetails.setTotalPrice(20);
        orders.add(orderDetails);
        when(orderDetailsService.getAllOrders()).thenReturn(orders);

        MvcResult result = this.mockMvc.perform(get("/api/orders/")).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

        List<OrderDetails> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<OrderDetails>>() {});

        assert(!actual.isEmpty());
    }

    @Test
    void testGetOrderDetailsById() throws Exception, InvalidOrderException {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setId(1);
        orderDetails.setPaymentType(PaymentType.CARD);
        orderDetails.setTotalPrice(20);
        when(orderDetailsService.getOrder(orderDetails.getId())).thenReturn(Optional.of(orderDetails));

        MvcResult result = this.mockMvc.perform(get("/api/orders/1")).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

        OrderDetails  actual = mapper.readValue(result.getResponse().getContentAsString(), OrderDetails.class);

        assert(actual.getTotalPrice() == 20);
    }
}