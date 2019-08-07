package com.mybatis.mybaties.service;

import com.mybatis.mybaties.utils.ResultUtil;

public interface BookInfoService {
    ResultUtil findCatalogByParentId(Integer parentId);
}
