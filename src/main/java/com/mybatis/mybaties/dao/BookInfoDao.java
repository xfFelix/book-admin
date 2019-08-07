package com.mybatis.mybaties.dao;

import com.mybatis.mybaties.entity.BookInfo;

import java.util.List;
import java.util.Map;

public interface BookInfoDao {
    int addBookInfo(BookInfo bookInfo);
    Integer findMaxNum(Integer id);
    Integer deleteBookInfo(Integer id);
    List<Map> findByParentId(int parentId);
}
