package com.mybatis.mybaties.service.impl;

import com.mybatis.mybaties.dao.HotListDao;
import com.mybatis.mybaties.entity.HotList;
import com.mybatis.mybaties.service.HotListService;
import com.mybatis.mybaties.utils.HttpClientUtil;
import com.mybatis.mybaties.utils.ResultUtil;
import com.mysql.cj.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HotListServiceImpl implements HotListService {

    @Autowired
    private HotListDao hotListDao;

    private String url;

    @Override
    public ResultUtil addHotList(String type){
        ResultUtil resultUtil = new ResultUtil();
        Integer newSize = 0;
        String json = HttpClientUtil.sendGet(HotList.HTTPURL, "type=" + type + "&key=" + HotList.HTTPURLKEY);
        JSONObject result = JSONObject.fromObject(json);
        JSONObject jsonObject = JSONObject.fromObject(result.get("result"));
        JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("data"));
        Object[] arr = jsonArray.toArray();
        if (arr.length == 0) {
            resultUtil.setCode(500);
            resultUtil.setMsg("没有最新的消息了");
            resultUtil.setData(false);
            return resultUtil;
        }
        for (int i = 0; i < arr.length; i++) {
            JSONObject data = JSONObject.fromObject(arr[i]);
            HotList hotList = new HotList();
            if (!StringUtils.isEmptyOrWhitespaceOnly(String.valueOf(data.get("uniquekey")))) {
                hotList.setUniquekey(String.valueOf(data.get("uniquekey")));
            }
            if (!StringUtils.isEmptyOrWhitespaceOnly(String.valueOf(data.get("title")))) {
                hotList.setTitle(String.valueOf(data.get("title")));
            }
            if (!StringUtils.isEmptyOrWhitespaceOnly(String.valueOf(data.get("date")))) {
                hotList.setDate(String.valueOf(data.get("date")));
            }
            if (!StringUtils.isEmptyOrWhitespaceOnly(String.valueOf(data.get("category")))) {
                hotList.setCategory(String.valueOf(data.get("category")));
            }
            if (!StringUtils.isEmptyOrWhitespaceOnly(String.valueOf(data.get("author_name")))) {
                hotList.setAuthorName(String.valueOf(data.get("author_name")));
            }
            if (!StringUtils.isEmptyOrWhitespaceOnly(String.valueOf(data.get("url")))) {
                hotList.setUrl(String.valueOf(data.get("url")));
            }
            if (!StringUtils.isEmptyOrWhitespaceOnly(String.valueOf(data.get("thumbnail_pic_s"))) && !"null".equals(String.valueOf(data.get("thumbnail_pic_s")))) {
                hotList.setThumbnailPicS(String.valueOf(data.get("thumbnail_pic_s")));
            }
            if (!StringUtils.isEmptyOrWhitespaceOnly(String.valueOf(data.get("thumbnail_pic_s02"))) && !"null".equals(String.valueOf(data.get("thumbnail_pic_s02")))) {
                hotList.setThumbnailPicS02(String.valueOf(data.get("thumbnail_pic_s02")));
            }
            if (!StringUtils.isEmptyOrWhitespaceOnly(String.valueOf(data.get("thumbnail_pic_s03"))) && !"null".equals(String.valueOf(data.get("thumbnail_pic_s03")))) {
                hotList.setThumbnailPicS03(String.valueOf(data.get("thumbnail_pic_s03")));
            }
            hotList.setCreateTime(new Date());
            hotList.setUpdateTime(new Date());
            hotList.setStatus(0);
            hotList.setType(type);
            boolean isRepeat = checkUniquekey(hotList.getUniquekey());
            if (isRepeat) {
                hotListDao.insertHotList(hotList);
                newSize++;
            } else {
                break;
            }
        }
        resultUtil.setData(false);
        resultUtil.setMsg("更新了" + newSize + "条信息");
        resultUtil.setCode(200);
        return resultUtil;
    }

    @Override
    public List<HotList> findAll() {
        List<HotList> list = new ArrayList<HotList>();
        list = hotListDao.findAll();
        return list;
    }

    public boolean checkUniquekey (String uniquekey) {
        List<String> uniqueList = hotListDao.findUnique();
        for (String unique : uniqueList) {
            if (unique.equals(uniquekey)) {
                return false;
            }
        }
        return true;
    }
}
