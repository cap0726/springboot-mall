package com.capliao.springbootmall.dao.impl;

import com.capliao.springbootmall.dao.OrderDao;
import com.capliao.springbootmall.model.Order;
import com.capliao.springbootmall.model.OrderItem;
import com.capliao.springbootmall.rowmapper.OrderItemRowMapper;
import com.capliao.springbootmall.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class OrderDaoimpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        String sql = "INSERT INTO `order` (user_id , total_amount , created_date , last_modified_date ) " +
                    " VALUES(:userId , :totalAmount , :createdDate , :lastModifiedDate )";

        HashMap<String , Object> map = new HashMap<>();
        map.put("userId" , userId);
        map.put("totalAmount" , totalAmount);

        Date now = new Date();
        map.put("createdDate" , now);
        map.put("lastModifiedDate" , now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql , new MapSqlParameterSource(map),keyHolder);

        int orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId ,List<OrderItem> orderItemList) {
        String sql = "INSERT INTO order_item(order_id , product_id , quantity , amount ) " +
                     " VALUES(:orderId , :productId , :quantity , :amount )" ;

        MapSqlParameterSource[] mapSqlParameterSource = new MapSqlParameterSource[orderItemList.size()];
        for(int i = 0 ; i < orderItemList.size() ; i++){
            OrderItem orderItem = orderItemList.get(i);

            mapSqlParameterSource[i] = new MapSqlParameterSource();
            mapSqlParameterSource[i].addValue("orderId" , orderId);
            mapSqlParameterSource[i].addValue("productId" , orderItem.getProductId());
            mapSqlParameterSource[i].addValue("quantity" , orderItem.getQuantity());
            mapSqlParameterSource[i].addValue("amount" , orderItem.getAmount());

        }

        namedParameterJdbcTemplate.batchUpdate(sql , mapSqlParameterSource);

    }

    @Override
    public Order getOrderByOrderId(Integer orderId) {
        String sql = "SELECT order_id , user_id , total_amount , created_date , last_modified_date  " +
                " FROM `order` WHERE order_id = :orderId";

        HashMap<String , Object> map = new HashMap<>();
        map.put("orderId" , orderId);

        List<Order> orderList = namedParameterJdbcTemplate.query(sql , map , new OrderRowMapper());

        if(orderList.size()>0){
            return orderList.get(0);
        }else{
            return null;
        }

    }

    @Override
    public List<OrderItem> getOrderItemByOrderId(Integer orderId) {
        String sql = "SELECT oi.order_item_id , oi.order_id , oi.product_id , oi.quantity , oi.amount , " +
                     " p.product_name , p.image_url " +
                     " FROM order_item as oi LEFT JOIN product as p on oi.product_id = p.product_id " +
                     " WHERE oi.order_id = :orderId";

        HashMap<String , Object> map = new HashMap<>();
        map.put("orderId" , orderId);

        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql , map , new OrderItemRowMapper());

        return orderItemList;
    }
}
