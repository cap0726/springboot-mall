package com.capliao.springbootmall.dao;

import com.capliao.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

}
