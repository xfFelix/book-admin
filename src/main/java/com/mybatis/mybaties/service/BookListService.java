package com.mybatis.mybaties.service;

import com.mybatis.mybaties.utils.ResultUtil;

import java.util.Map;

public interface BookListService {
    ResultUtil findAll(Map<String, Object> map);

    ResultUtil addXinBiQuBook(String identify, String urlId);

    ResultUtil updateStaus(Integer id);

    ResultUtil addJingCaiBook(String identify, String urlId);

    ResultUtil updateBook(String urlId, Integer id);

    ResultUtil addQuanBenBook(String identify, String urlId);

    ResultUtil addMianHuaTang(String identify, String urlId);
}
