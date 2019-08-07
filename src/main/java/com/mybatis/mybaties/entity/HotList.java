package com.mybatis.mybaties.entity;

import java.util.Date;

public class HotList {
    private Integer id;
    private String uniquekey;
    private String title;
    private String date;
    private String category;
    private String authorName;
    private String url;
    private String thumbnailPicS;
    private String thumbnailPicS02;
    private String thumbnailPicS03;
    private Date createTime;
    private Date updateTime;
    private Integer status;
    private String Type;
    public static final  String TOP = "top";
    public static final  String JUNSHI = "junshi";
    public static final  String TIYU = "tiyu";
    public static final  String YULE = "yule";
    public static final String HTTPURL = "https://v.juhe.cn/toutiao/index";
    public static final String HTTPURLKEY = "fc35d7872c25744ab4669c7d9dbcf15e";

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailPicS() {
        return thumbnailPicS;
    }

    public void setThumbnailPicS(String thumbnailPicS) {
        this.thumbnailPicS = thumbnailPicS;
    }

    public String getThumbnailPicS02() {
        return thumbnailPicS02;
    }

    public void setThumbnailPicS02(String thumbnailPicS02) {
        this.thumbnailPicS02 = thumbnailPicS02;
    }

    public String getThumbnailPicS03() {
        return thumbnailPicS03;
    }

    public void setThumbnailPicS03(String thumbnailPicS03) {
        this.thumbnailPicS03 = thumbnailPicS03;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
