package com.capliao.springbootmall.service.impl;

import com.capliao.springbootmall.dao.UserDao;
import com.capliao.springbootmall.dto.UserLoginRequest;
import com.capliao.springbootmall.dto.UserRegisterRequest;
import com.capliao.springbootmall.model.User;
import com.capliao.springbootmall.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceimpl implements UserService {

    @Autowired
    private UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(UserServiceimpl.class);

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {

        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if(user != null){
            log.warn("此 {} 已被申請過!" , userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {

        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        if(user == null){
            log.warn("此 {} 帳號尚未註冊!" , userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if(user.getPassword().equals(userLoginRequest.getPassword())){
            log.info("登入成功");
            return user;
        }else{
            log.warn("密碼錯誤!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


    }
}
