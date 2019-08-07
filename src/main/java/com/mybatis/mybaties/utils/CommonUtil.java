package com.mybatis.mybaties.utils;

import java.util.UUID;

public class CommonUtil {

    public static final Integer STATUS_OK=200;

    public static final Integer STATUS_ERROR=500;

    public static final Integer STATUS_SAVE = 0; // 保存

    public static final Integer STATUS_DELETE = 1; // 删除

    public static String getUUID(){
        String id = UUID.randomUUID().toString().replaceAll("-","");
        return id;
    }

    public static Integer getPageStart(Integer page, Integer limit) {
        Integer start = limit * (page-1);
        return start;
    }

}
