package com.mybatis.mybaties.service;

import com.mybatis.mybaties.entity.User;
import com.mybatis.mybaties.utils.ResultUtil;

public interface UserService {
    ResultUtil addUser(String name, String password);
    ResultUtil findAll();
    ResultUtil checkUser(String name, String password);

    ResultUtil findByToken(String token);

    ResultUtil update(User user);
}
