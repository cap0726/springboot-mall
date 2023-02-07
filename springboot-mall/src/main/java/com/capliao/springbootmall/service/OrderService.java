package com.capliao.springbootmall.service;


import com.capliao.springbootmall.dto.CreateOrderRequest;
import com.capliao.springbootmall.model.Order;

public interface OrderService {

    Integer createOrder(Integer userId , CreateOrderRequest createOrderRequest);

    Order  getOrderByOrderId(Integer orderId);

}
