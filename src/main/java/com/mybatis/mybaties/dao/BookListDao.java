package com.mybatis.mybaties.dao;

import com.mybatis.mybaties.entity.BookList;
import com.mybatis.mybaties.entity.BookUrlList;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BookListDao {
    List<BookList> findAllBookByLimit(Map map);
    List<BookList> findAllBookList();
    int insertBookList(BookList bookList);
    BookUrlList findUrlById(String id);
    BookList findListById(Integer id);
    int updateStatus(BookList bookList);
    int deleteBook(Integer id);
    void updateBook(BookList bookList);
    Integer findCount();
    int updateUrlById(@Param("params") Map<String, Object> map);
}
