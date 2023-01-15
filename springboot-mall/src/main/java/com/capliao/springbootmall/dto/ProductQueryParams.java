package com.capliao.springbootmall.dto;

import com.capliao.springbootmall.constant.ProductCategory;

public class ProductQueryParams {

    private ProductCategory productCategory;
    private String search;

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
