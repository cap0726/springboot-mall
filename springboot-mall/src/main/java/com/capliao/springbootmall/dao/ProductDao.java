package com.capliao.springbootmall.dao;

import com.capliao.springbootmall.dto.ProductRequest;
import com.capliao.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId , ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
