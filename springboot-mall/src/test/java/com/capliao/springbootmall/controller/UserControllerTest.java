package com.capliao.springbootmall.controller;

import com.capliao.springbootmall.dao.UserDao;
import com.capliao.springbootmall.dto.UserRegisterRequest;
import com.capliao.springbootmall.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void register_User_success () throws Exception{
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test1@gmail.com");
        userRegisterRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
        post("/users/register").
                contentType(MediaType.APPLICATION_JSON).
                content(json);

        mockMvc.perform(requestBuilder).
                andDo(print()).
                andExpect(status().isCreated()).
                andExpect(jsonPath("userId" , notNullValue())).
                andExpect(jsonPath("email" , equalTo("test1@gmail.com"))).
                andExpect(jsonPath("createdDate" , notNullValue())).
                andExpect(jsonPath("lastModifiedDate" , notNullValue()));
        //檢查資料庫密碼不為名碼
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        assertNotEquals(userRegisterRequest.getPassword() , user.getPassword());
    }

    @Test
    public void register_InvalidEmailFormat() throws Exception{
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("354qw3458");
        userRegisterRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/users/register").
                contentType(MediaType.APPLICATION_JSON).
                content(json);

        mockMvc.perform(requestBuilder).
                andExpect(status().is(400));

    }

    @Test
    public void register_AlreadyExist() throws Exception{
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test1@gmail.com");
        userRegisterRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/users/register").
                contentType(MediaType.APPLICATION_JSON).
                content(json);

        mockMvc.perform(requestBuilder).andExpect(status().isCreated());

        mockMvc.perform(requestBuilder).andExpect(status().is(400));

    }

    @Test
    public void login_success() throws Exception{
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test2@gmail.com");
        userRegisterRequest.setPassword("123");

        String json = register(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/users/login").
                contentType(MediaType.APPLICATION_JSON).
                content(json);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

    }

    @Test
    public void login_WrongPassword() throws Exception{
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test3@gmail.com");
        userRegisterRequest.setPassword("123");

        String old_json = register(userRegisterRequest);
        userRegisterRequest.setPassword("456");
        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/users/login").
                contentType(MediaType.APPLICATION_JSON).
                content(json);

        mockMvc.perform(requestBuilder).andExpect(status().is(400));

    }

    @Test
    public void login_NoEmailRegister() throws Exception{
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test4@gmail.com");
        userRegisterRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/users/login").
                contentType(MediaType.APPLICATION_JSON).
                content(json);

        mockMvc.perform(requestBuilder).andExpect(status().is(400));

    }

    private String register(UserRegisterRequest registerRequest) throws Exception{
        String json = objectMapper.writeValueAsString(registerRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                post("/users/register").
                contentType(MediaType.APPLICATION_STREAM_JSON).
                content(json);

        mockMvc.perform(requestBuilder);
        return json;
    }




}