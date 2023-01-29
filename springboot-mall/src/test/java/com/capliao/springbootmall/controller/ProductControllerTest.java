package com.capliao.springbootmall.controller;

import com.capliao.springbootmall.constant.ProductCategory;
import com.capliao.springbootmall.dto.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void get_Product_success() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/products/{productId}" ,1 );

        mockMvc.perform(requestBuilder).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.productName" , equalTo("蘋果（澳洲）"))).
                andExpect(jsonPath("$.category" , equalTo("FOOD"))).
                andExpect(jsonPath("$.imageUrl" , notNullValue())).
                andExpect(jsonPath("$.price" , notNullValue()));

    }

    @Test
    public void get_Product_Fail() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/products/{productId}" ,100 );

        mockMvc.perform(requestBuilder).andExpect(status().is(404));
    }


    @Transactional
    @Test
    public void create_Product_success() throws Exception{
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test FOOD");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("http://test.com");
        productRequest.setPrice(200);
        productRequest.setStock(2);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/products").
                contentType(MediaType.APPLICATION_JSON).
                content(json);

        mockMvc.perform(requestBuilder).
                andDo(print()).
                andExpect(status().is(201)).
                andExpect(jsonPath("$.productName" , equalTo("test FOOD"))).
                andExpect(jsonPath("$.category" , equalTo("FOOD"))).
                andExpect(jsonPath("$.price" , equalTo(200))).
                andExpect(jsonPath("$.stock" , equalTo(2))).
                andExpect(jsonPath("$.createdDate" , notNullValue())).
                andExpect(jsonPath("$.lastModifiedDate" , notNullValue()));


    }

    @Transactional
    @Test
    public void create_Product_Fail() throws Exception{
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test FOOD");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/products").
                contentType(MediaType.APPLICATION_JSON).
                content(json);

        mockMvc.perform(requestBuilder).
                andExpect(status().is(400));

    }

    @Transactional
    @Test
    public void update_Product_success() throws Exception{
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test FOOD");
        productRequest.setCategory(ProductCategory.CAR);
        productRequest.setImageUrl("www");
        productRequest.setPrice(200);
        productRequest.setStock(20);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                put("/products/{productId}" , 1).
                contentType(MediaType.APPLICATION_JSON).
                content(json);

        mockMvc.perform(requestBuilder).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.productName" , equalTo("test FOOD"))).
                andExpect(jsonPath("$.category" , equalTo("CAR"))).
                andExpect(jsonPath("$.price" , equalTo(200))).
                andExpect(jsonPath("$.lastModifiedDate" , notNullValue()));


    }

    @Transactional
    @Test
    public void update_Product_Fail() throws Exception{
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test FOOD");
        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                put("/products/{productId}" , 1).
                contentType(MediaType.APPLICATION_JSON).
                content(json);

        mockMvc.perform(requestBuilder).
                andExpect(status().is(400));

    }

    @Transactional
    @Test
    public void delete_Product_success() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                delete("/products/{productId}" , 1);

        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
    }

    @Transactional
    @Test
    public void delete_product_fail() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                delete("/products/{productId}" , 100);

        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
    }


    @Test
    public void get_Products_success() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/products");

        mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andDo(print()).
                andExpect(jsonPath("$.limit" , equalTo(5))).
                andExpect(jsonPath("$.offset" , equalTo(0))).
                andExpect(jsonPath("$.result[1].productName" , equalTo("Benz")));
    }

    @Test
    public void products_sorting() throws Exception{


        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/products").
                param("orderBy" , "price");

        mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andDo(print()).
                andExpect(jsonPath("$.limit" , equalTo(5))).
                andExpect(jsonPath("$.offset" , equalTo(0))).
                andExpect(jsonPath("$.result[1].productName" , equalTo("BMW")));
    }

    @Test
    public void products_filtering() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/products").
                param("category" , "FOOD").
                param("search" , "蘋果");

        mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andDo(print()).
                andExpect(jsonPath("$.limit" , equalTo(5))).
                andExpect(jsonPath("$.offset" , equalTo(0))).
                andExpect(jsonPath("$.result[0].productName" , equalTo("好吃又鮮甜的蘋果橘子")));
    }

    @Test
    public void products_page() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/products").
                param("limit" , "4").
                param("offset" , "1");

        mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andDo(print()).
                andExpect(jsonPath("$.limit" , equalTo(4))).
                andExpect(jsonPath("$.offset" , equalTo(1))).
                andExpect(jsonPath("$.total" , equalTo(7))).
                andExpect(jsonPath("$.result[2].productName" , equalTo("Toyota")));
    }
}