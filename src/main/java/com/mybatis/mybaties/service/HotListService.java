package com.mybatis.mybaties.service;

import com.mybatis.mybaties.entity.HotList;
import com.mybatis.mybaties.utils.ResultUtil;

import java.util.List;

public interface HotListService {
    ResultUtil addHotList(String type);
    List<HotList> findAll();
}
