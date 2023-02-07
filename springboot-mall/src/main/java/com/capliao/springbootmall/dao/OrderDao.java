package com.capliao.springbootmall.dao;

import com.capliao.springbootmall.model.Order;
import com.capliao.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId , Integer totalAmount);

    void createOrderItems(Integer orderId , List<OrderItem> orderItemList);

    Order getOrderByOrderId(Integer orderId);

    List<OrderItem> getOrderItemByOrderId(Integer orderId);

}
