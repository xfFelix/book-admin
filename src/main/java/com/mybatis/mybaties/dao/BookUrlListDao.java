package com.mybatis.mybaties.dao;

import com.mybatis.mybaties.entity.BookUrlList;

import java.util.List;

public interface BookUrlListDao {
    int insertBookUrl(BookUrlList bookUrlList);
    List<BookUrlList> findAll();
}
