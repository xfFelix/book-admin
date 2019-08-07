package com.mybatis.mybaties.web;

import com.mybatis.mybaties.entity.User;
import com.mybatis.mybaties.service.UserService;
import com.mybatis.mybaties.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/adduser")
    public ResultUtil addUser(@RequestParam String name, String password){
        ResultUtil result = userService.addUser(name, password);
        return result;
    }

    @PostMapping("/update")
    public ResultUtil update(@RequestBody User user){
        ResultUtil result = userService.update(user);
        return result;
    }

    @GetMapping("/findall")
    public ResultUtil findAll(){
        ResultUtil result = userService.findAll();
        return result;
    }

    @PostMapping("/checkuser")
    public ResultUtil checkUser(@RequestBody Map<String, String> map){
        String username = map.get("username");
        String password = map.get("password");
        ResultUtil result = userService.checkUser(username, password);
        return result;
    }

    @PostMapping("/findbytoken")
    public ResultUtil findByToken(@RequestHeader("Access-Token")  String token){
        ResultUtil result = userService.findByToken(token);
        return result;
    }
}
