package com.capliao.springbootmall.controller;



import com.capliao.springbootmall.dto.CreateOrderRequest;
import com.capliao.springbootmall.dto.OrderQueryParams;
import com.capliao.springbootmall.model.Order;
import com.capliao.springbootmall.service.OrderService;
import com.capliao.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

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


    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @PathVariable Integer userId,
            @RequestParam (defaultValue  = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam (defaultValue = "0") @Min(0) Integer offset
    ){

        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        int total = orderService.countOrder(orderQueryParams);
        List<Order> orderList = orderService.getOrders(orderQueryParams);

        Page<Order> page = new Page();
        page.setTotal(total);
        page.setLimit(limit);
        page.setOffset(offset);
        page.setResult(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }


}
