package com.mybatis.mybaties.web;

import com.mybatis.mybaties.service.BookUrlListService;
import com.mybatis.mybaties.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookurl")
public class BookUrlListController {

    @Autowired
    private BookUrlListService bookUrlListService;

    @GetMapping("/addurl")
    public ResultUtil addUrl(@RequestParam String url, String name){
        ResultUtil result = bookUrlListService.addBookUrl(url,name);
        return result;
    }

    @GetMapping("/findall")
    public ResultUtil findAll(){
        ResultUtil result = bookUrlListService.findAll();
        return result;
    }

}
