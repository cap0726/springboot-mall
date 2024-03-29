package com.capliao.springbootmall.service;

import com.capliao.springbootmall.dto.ProductQueryParams;
import com.capliao.springbootmall.dto.ProductRequest;
import com.capliao.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    Integer countProducts(ProductQueryParams productQueryParams);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId , ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
