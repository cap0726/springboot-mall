package com.capliao.springbootmall.service.impl;

import com.capliao.springbootmall.dao.OrderDao;
import com.capliao.springbootmall.dao.ProductDao;

import com.capliao.springbootmall.dao.UserDao;
import com.capliao.springbootmall.dto.BuyItems;
import com.capliao.springbootmall.dto.CreateOrderRequest;
import com.capliao.springbootmall.dto.OrderQueryParams;
import com.capliao.springbootmall.model.Order;
import com.capliao.springbootmall.model.OrderItem;
import com.capliao.springbootmall.model.Product;
import com.capliao.springbootmall.model.User;
import com.capliao.springbootmall.service.OrderService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceimpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(OrderServiceimpl.class);

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
        //判斷User是否存在
        User user = userDao.getUserById(userId);
        if(user == null){
            log.warn("此User ID {} 不存在!!" , userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();


        for(BuyItems buyItems : createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItems.getProductId());

            //判斷商品是否存在和庫存是否足夠
            if(product == null){
                log.warn("此商品 {} 不存在!!" , product.getProductName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if(product.getStock() < buyItems.getQuantity()){
                log.warn("此商品庫存不足!!");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            productDao.updateStock(buyItems.getProductId() , product.getStock() - buyItems.getQuantity());

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

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {

        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for(Order order : orderList){
            List<OrderItem> orderItemList = orderDao.getOrderItemByOrderId(order.getOrderID());
            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }
}
