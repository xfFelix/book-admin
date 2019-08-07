package com.mybatis.mybaties.web;

import com.mybatis.mybaties.service.BookInfoService;
import com.mybatis.mybaties.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/bookinfo")
public class BookInfoController {

    @Autowired
    private BookInfoService bookInfoService;

    @PostMapping("/findcatalog")
    public ResultUtil findCatalogByParentId(@RequestBody Map<String,Integer> map) {
        Integer parentId = map.get("parentId");
        ResultUtil resultUtil = bookInfoService.findCatalogByParentId(parentId);
        return resultUtil;
    }

}
