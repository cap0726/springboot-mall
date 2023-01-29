package com.capliao.springbootmall.dao;

import com.capliao.springbootmall.dto.UserRegisterRequest;
import com.capliao.springbootmall.model.User;
import org.springframework.stereotype.Component;


public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

}
