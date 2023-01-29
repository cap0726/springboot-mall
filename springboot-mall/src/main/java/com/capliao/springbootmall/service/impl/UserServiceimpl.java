package com.capliao.springbootmall.service.impl;

import com.capliao.springbootmall.dao.UserDao;
import com.capliao.springbootmall.dto.UserRegisterRequest;
import com.capliao.springbootmall.model.User;
import com.capliao.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceimpl implements UserService {

    @Autowired
    private UserDao userDao;



    @Override
    public Integer register(UserRegisterRequest registerRequest) {
        return userDao.createUser(registerRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
}
