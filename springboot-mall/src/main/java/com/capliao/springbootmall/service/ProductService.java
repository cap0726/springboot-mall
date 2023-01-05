package com.capliao.springbootmall.service;

import com.capliao.springbootmall.dao.ProductDao;
import com.capliao.springbootmall.dto.ProductRequest;
import com.capliao.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId , ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
