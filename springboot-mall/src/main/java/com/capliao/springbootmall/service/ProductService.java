package com.capliao.springbootmall.service;

import com.capliao.springbootmall.dao.ProductDao;
import com.capliao.springbootmall.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);

}
