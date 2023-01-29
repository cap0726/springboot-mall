package com.capliao.springbootmall.service;

import com.capliao.springbootmall.dto.UserRegisterRequest;
import com.capliao.springbootmall.model.User;
import org.springframework.stereotype.Component;


public interface UserService {

    Integer register(UserRegisterRequest registerRequest);


    User getUserById(Integer userId);

}
