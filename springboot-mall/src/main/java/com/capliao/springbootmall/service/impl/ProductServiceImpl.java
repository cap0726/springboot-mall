package com.capliao.springbootmall.service.impl;

import com.capliao.springbootmall.dao.ProductDao;
import com.capliao.springbootmall.dto.ProductRequest;
import com.capliao.springbootmall.model.Product;
import com.capliao.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }


    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }
}
