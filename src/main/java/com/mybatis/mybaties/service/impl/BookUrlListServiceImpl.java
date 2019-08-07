package com.mybatis.mybaties.service.impl;

import com.mybatis.mybaties.dao.BookUrlListDao;
import com.mybatis.mybaties.entity.BookUrlList;
import com.mybatis.mybaties.service.BookUrlListService;
import com.mybatis.mybaties.utils.CommonUtil;
import com.mybatis.mybaties.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookUrlListServiceImpl implements BookUrlListService {

    @Autowired
    private BookUrlListDao bookUrlListDao;

    @Override
    public ResultUtil addBookUrl(String url, String name) {
        ResultUtil result = new ResultUtil();
        BookUrlList bookUrlList = new BookUrlList();
        try {
            bookUrlList.setId(CommonUtil.getUUID());
            bookUrlList.setName(name);
            bookUrlList.setUrl(url);
            bookUrlList.setCreateTime(new Date());
            bookUrlList.setUpdateTime(new Date());
            bookUrlListDao.insertBookUrl(bookUrlList);
            result.setCode(200);
            result.setMsg("保存成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setErrMsg(e.getMessage());
            return result;
        }
    }

    @Override
    public ResultUtil findAll() {
        ResultUtil result = new ResultUtil();
        List<BookUrlList> list= bookUrlListDao.findAll();
        result.setCode(200);
        result.setData(list);
        result.setMsg("获取url列表成功");
        return result;
    }
}
