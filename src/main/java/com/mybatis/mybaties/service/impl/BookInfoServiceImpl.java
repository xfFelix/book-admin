package com.mybatis.mybaties.service.impl;

import com.mybatis.mybaties.dao.BookInfoDao;
import com.mybatis.mybaties.dao.BookListDao;
import com.mybatis.mybaties.entity.BookList;
import com.mybatis.mybaties.service.BookInfoService;
import com.mybatis.mybaties.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookInfoServiceImpl implements BookInfoService {

    @Autowired
    private BookInfoDao bookInfoDao;

    @Autowired
    private BookListDao bookListDao;

    @Override
    public ResultUtil findCatalogByParentId(Integer parentId) {
        ResultUtil resultUtil = new ResultUtil();
        Map<String, Object> map = new HashMap<>();
        List<Map> list= bookInfoDao.findByParentId(parentId);
        BookList bookList = bookListDao.findListById(parentId);
        map.put("list", list);
        map.put("name", bookList.getName());
        resultUtil.setCode(200);
        resultUtil.setData(map);
        resultUtil.setMsg("获取目录成功");
        return resultUtil;
    }
}
