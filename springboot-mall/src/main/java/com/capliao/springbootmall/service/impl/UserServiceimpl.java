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
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class UserServiceimpl implements UserService {

    @Autowired
    private UserDao userDao;

   //BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final static Logger log = LoggerFactory.getLogger(UserServiceimpl.class);

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {

        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        //檢查user是否被註冊過
        if(user != null){
            log.warn("此 {} 已被申請過!" , userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //密碼 MD5加密
        //String hashPassword = bCryptPasswordEncoder.encode(userRegisterRequest.getPassword());
        String hashPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        //System.out.println(hashPassword);
        userRegisterRequest.setPassword(hashPassword);

        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {

        User user = userDao.getUserByEmail(userLoginRequest.getEmail());


        //檢查user是否存在
        if(user == null){
            log.warn("此 {} 帳號尚未註冊!" , userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //轉換Hash密碼

        //String hashPassword = bCryptPasswordEncoder.encode(userLoginRequest.getPassword());
        String hashPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        //檢查密碼
        if(user.getPassword().equals(hashPassword)){
            log.info("登入成功");
            return user;
        }else{
            log.warn("密碼錯誤!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

/*    private String getMd5AddSaltPassword(String oldPassword){
        String salt = UUID.randomUUID().toString().toUpperCase();
        System.out.println(salt);
        //生成加密加鹽密碼
        String hashPassword = DigestUtils.md5DigestAsHex((salt + oldPassword +salt).getBytes());

        return hashPassword;

    }*/
}
