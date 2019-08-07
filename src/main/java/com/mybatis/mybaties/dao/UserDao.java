package com.mybatis.mybaties.dao;

import com.mybatis.mybaties.entity.User;

import java.util.List;

public interface UserDao {
    int insertUser(User user);
    List<User> findAll();
    User findById(String id);
    int updateById(User user);
}
