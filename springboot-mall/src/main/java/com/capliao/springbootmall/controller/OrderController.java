package com.capliao.springbootmall.controller;



import com.capliao.springbootmall.dto.CreateOrderRequest;
import com.capliao.springbootmall.model.Order;
import com.capliao.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/order")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId ,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){


        Integer orderId = orderService.createOrder(userId , createOrderRequest);

        Order order = orderService.getOrderByOrderId(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);

    }


}
