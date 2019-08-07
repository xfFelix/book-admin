package com.mybatis.mybaties.service.impl;

import com.mybatis.mybaties.dao.UserDao;
import com.mybatis.mybaties.entity.User;
import com.mybatis.mybaties.service.UserService;
import com.mybatis.mybaties.utils.CommonUtil;
import com.mybatis.mybaties.utils.ResultUtil;
import com.mybatis.mybaties.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public ResultUtil addUser(String name, String password) {
        ResultUtil result = new ResultUtil();
        User user = new User();
        user.setId(CommonUtil.getUUID());
        user.setName(name);
        user.setPassword(password);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int num = userDao.insertUser(user);
        result.setCode(200);
        result.setData(num);
        result.setMsg("保存成功");
        return result;
    }

    @Override
    public ResultUtil findAll() {
        ResultUtil result = new ResultUtil();
        List<User> list = userDao.findAll();
        result.setCode(200);
        result.setData(list);
        result.setMsg("获取信息列表成功");
        return result;
    }

    @Override
    public ResultUtil findByToken(String token) {
        ResultUtil result = new ResultUtil();
        User user = userDao.findById(TokenUtil.getUserId(token));

        List<String> list = new ArrayList<>();
        list.add("admin");

        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("name", user.getName());
        map.put("avatar", user.getAvatar());
        map.put("introduction", user.getIntroduction());
        map.put("roles", list);

        result.setCode(200);
        result.setData(map);
        result.setMsg("获取成功");
        return result;
    }

    @Override
    public ResultUtil update(User user) {
        ResultUtil result = new ResultUtil();
        int i = userDao.updateById(user);
        result.setCode(200);
        result.setData(i);
        result.setMsg("更新用户成功");
        return result;
    }

    @Override
    public ResultUtil checkUser(String name, String password) {
        ResultUtil result = new ResultUtil();
        List<User> list = userDao.findAll();
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            if (user.getName().equals(name) && user.getPassword().equals(password)) {
                try {
                    String token = TokenUtil.sign(user.getName(), user.getId());
                    result.setCode(200);
                    result.setData(token);
                    result.setMsg("验证通过");
                    return result;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    result.setCode(500);
                    result.setData("fail");
                    result.setErrMsg(e.getMessage());
                    return result;
                }
            }
        }
        result.setCode(500);
        result.setErrMsg("请先注册");
        return result;
    }
}
