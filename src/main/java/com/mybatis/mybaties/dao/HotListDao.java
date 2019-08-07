package com.mybatis.mybaties.dao;

import com.mybatis.mybaties.entity.HotList;

import java.util.List;

public interface HotListDao {
    int insertHotList(HotList hotList);
    List<String> findUnique();
    List<HotList> findAll();
}
