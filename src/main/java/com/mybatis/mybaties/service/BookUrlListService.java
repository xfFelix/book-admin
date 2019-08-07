package com.mybatis.mybaties.service;

import com.mybatis.mybaties.utils.ResultUtil;

public interface BookUrlListService {
    ResultUtil addBookUrl(String url, String name);
    ResultUtil findAll();
}
