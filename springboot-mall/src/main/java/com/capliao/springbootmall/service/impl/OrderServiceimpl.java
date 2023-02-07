package com.capliao.springbootmall.service.impl;

import com.capliao.springbootmall.dao.OrderDao;
import com.capliao.springbootmall.dao.ProductDao;

import com.capliao.springbootmall.dto.BuyItems;
import com.capliao.springbootmall.dto.CreateOrderRequest;
import com.capliao.springbootmall.model.Order;
import com.capliao.springbootmall.model.OrderItem;
import com.capliao.springbootmall.model.Product;
import com.capliao.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceimpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public Order getOrderByOrderId(Integer orderId) {
        Order order = orderDao.getOrderByOrderId(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemByOrderId(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();


        for(BuyItems buyItems : createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItems.getProductId());

            //計算總價錢
            int amount = product.getPrice() * buyItems.getQuantity();
            totalAmount = totalAmount + amount;

            //將buy items轉換成order items
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItems.getProductId());
            orderItem.setQuantity(buyItems.getQuantity());
            orderItem.setAmount(amount);
            orderItemList.add(orderItem);

        }

        Integer orderId =  orderDao.createOrder(userId , totalAmount);
        orderDao.createOrderItems(orderId , orderItemList);

        return orderId;
    }
}
