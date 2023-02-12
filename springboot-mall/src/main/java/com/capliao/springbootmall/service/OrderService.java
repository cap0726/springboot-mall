package com.capliao.springbootmall.service;


import com.capliao.springbootmall.dto.CreateOrderRequest;
import com.capliao.springbootmall.dto.OrderQueryParams;
import com.capliao.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer createOrder(Integer userId , CreateOrderRequest createOrderRequest);

    Order  getOrderByOrderId(Integer orderId);

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

}
