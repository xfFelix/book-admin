package com.mybatis.mybaties.service.impl;

import com.mybatis.mybaties.dao.BookInfoDao;
import com.mybatis.mybaties.dao.BookListDao;
import com.mybatis.mybaties.entity.BookList;
import com.mybatis.mybaties.entity.BookUrlList;
import com.mybatis.mybaties.service.BookListService;
import com.mybatis.mybaties.thread.JingCaiThread;
import com.mybatis.mybaties.thread.XinBiQuThread;
import com.mybatis.mybaties.utils.CommonUtil;
import com.mybatis.mybaties.utils.ResultUtil;
import com.mysql.cj.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookListServiceImpl implements BookListService {

    @Autowired
    private BookListDao bookListDao;

    @Autowired
    private BookInfoDao bookInfoDao;

    @Override
    public ResultUtil findAll(Map<String, Object> initMap) {
        ResultUtil resultUtil = new ResultUtil();
        List<BookList> list = bookListDao.findAllBookByLimit(initMap);
        Integer count = bookListDao.findCount();
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("item", list);
        map.put("count", count);
        resultUtil.setCode(200);
        resultUtil.setData(map);
        resultUtil.setMsg("获取书籍列表成功");
        return resultUtil;
    }

    /**
     * 新笔趣小说网-添加小说
     * @param identify
     * @param urlId
     * @return
     */
    @Override
    public ResultUtil addXinBiQuBook(String identify, String urlId) {
        ResultUtil resultUtil = new ResultUtil();
        BookList bookList =new BookList();
        BookUrlList bookUrlList = bookListDao.findUrlById(urlId);
        Document document = null;
        try {
            document = Jsoup.connect(bookUrlList.getUrl() + identify).get();
            // 书名
            String name = document.getElementsByTag("h1").get(0).text();

            // 作者
            Elements info = document.select("div[id=info]");
            String author = info.get(0).child(1).text().split("：")[1];
            // 简介
            Elements intro = document.select("div[id=intro]");
            String introduction = intro.get(0).child(0).text();

            List<Element> list = document.getElementsByTag("dd");

            // 章节数量
            Integer count = list.size();
            if (StringUtils.isEmptyOrWhitespaceOnly(name)){
                resultUtil.setCode(500);
                resultUtil.setMsg("该网站没有此类书籍");
                return resultUtil;
            }

            bookList.setName(name);
            bookList.setParentId(urlId);
            bookList.setSize(count);
            bookList.setUrl(identify);
            bookList.setUpdateTime(new Date());
            bookList.setAuthor(author);
            bookList.setIntro(introduction);

            Thread xinBiQuThread;
            if (checkIdentify(identify) == null) {
                bookList.setStatus(CommonUtil.STATUS_SAVE);
                bookList.setCreateTime(new Date());
                int i =bookListDao.insertBookList(bookList);
                xinBiQuThread = new XinBiQuThread(document,bookUrlList.getUrl(), bookList.getId());
            } else {
                bookListDao.updateBook(bookList);
                xinBiQuThread = new XinBiQuThread(document,bookUrlList.getUrl(), checkIdentify(identify));
            }

            xinBiQuThread.setName("新笔趣");
            xinBiQuThread.start();

            resultUtil.setMsg("插入成功");
            resultUtil.setCode(200);
            resultUtil.setData(true);
            return resultUtil;
        } catch (IOException e) {
            e.printStackTrace();
            resultUtil.setCode(500);
            resultUtil.setData(false);
            resultUtil.setMsg("添加失败");
            return resultUtil;
        }
    }

    /**
     * 删除操作（逻辑删除和物理删除）
     * @param id
     * @return
     */
    @Override
    public ResultUtil updateStaus(Integer id) {
        ResultUtil res= new ResultUtil();
        BookList bookList = bookListDao.findListById(id);
        if (bookList.getStatus() == CommonUtil.STATUS_SAVE) {
            bookList.setStatus(CommonUtil.STATUS_DELETE);
            int result = bookListDao.updateStatus(bookList);
        } else if (bookList.getStatus() == CommonUtil.STATUS_DELETE) {
            int result = bookListDao.deleteBook(id);
            int resultInfo = bookInfoDao.deleteBookInfo(bookList.getId());
        }
        res.setCode(200);
        res.setData(true);
        res.setMsg("操作成功");
        return res;
    }

    /**
     * 精彩小说网-添加小说
     * @param identify
     * @param urlId
     * @return
     */
    @Override
    public ResultUtil addJingCaiBook(String identify, String urlId) {
        ResultUtil resultUtil = new ResultUtil();
        BookList bookList =new BookList();
        BookUrlList bookUrlList = bookListDao.findUrlById(urlId);
        Document document = null;
        try {
            document = Jsoup.connect(bookUrlList.getUrl() + identify).get();
            String name = document.select("h1").get(0).text();
            List<Element> list = document.select("a[class=text-danger]");
            Element element = list.get(1);
            String string = element.attr("href");
            String[] numlist = string.split("\\.")[0].split("\\/");
            // 章节
            Integer count = Integer.parseInt(numlist[numlist.length - 1]);
            // 作者
            String author = document.select("p[class=text-overflow]").get(0).children().text();
            // 简介 文本
            String intro = document.select("p[id=bookIntro]").get(0).text();
            String introHtml = document.select("p[id=bookIntro]").get(0).html();
            if (StringUtils.isEmptyOrWhitespaceOnly(name)){
                resultUtil.setCode(500);
                resultUtil.setMsg("该网站没有此类书籍");
                return resultUtil;
            }
            Thread jingCaiThread = new JingCaiThread(document, bookUrlList.getUrl());
            jingCaiThread.setName("精彩小说网");
            jingCaiThread.start();

            bookList.setName(name);
            bookList.setParentId(urlId);
            bookList.setSize(count);
            bookList.setUrl(identify);
            bookList.setCreateTime(new Date());
            bookList.setUpdateTime(new Date());
            bookList.setAuthor(author);
            bookList.setIntro(intro);
            bookList.setStatus(CommonUtil.STATUS_SAVE);
            int i =bookListDao.insertBookList(bookList);
            resultUtil.setMsg("插入成功");
            resultUtil.setCode(200);
            resultUtil.setData(i);
            return resultUtil;
        } catch (IOException e) {
            e.printStackTrace();
            resultUtil.setCode(500);
            resultUtil.setData(false);
            resultUtil.setMsg("添加失败");
            return resultUtil;
        }
    }

    /**
     * 验证是否存在书籍
     * @param identify 书本标识
     * @return true已存在 false不存在
     */
    public Integer checkIdentify(String identify) {
        List<BookList> bs = bookListDao.findAllBookList();
        for (int i = 0; i < bs.size(); i++) {
            if (bs.get(i).getUrl().equals(identify)) {
                return bs.get(i).getId();
            }
        }
        return null;
    }

    @Override
    public ResultUtil updateBook(String urlId, Integer id) {
        ResultUtil resultUtil = new ResultUtil();
        BookUrlList bookUrlList = bookListDao.findUrlById(urlId);
        BookList bookList = bookListDao.findListById(id);
        Document document = null;
        try {
            document = Jsoup.connect(bookUrlList.getUrl() + bookList.getUrl()).get();
            // 书名
            String name = document.getElementsByTag("h1").get(0).text();

            // 作者
            Elements info = document.select("div[id=info]");
            String author = info.get(0).child(1).text().split("：")[1];
            // 简介
            Elements intro = document.select("div[id=intro]");
            String introduction = intro.get(0).child(0).text();

            List<Element> list = document.getElementsByTag("dd");

            // 章节数量
            Integer count = list.size();

            bookList.setName(name);
            bookList.setSize(count);
            bookList.setUpdateTime(new Date());
            bookList.setAuthor(author);
            bookList.setIntro(introduction);
            bookList.setBadge(0);

            Thread xinBiQuThread;
            bookListDao.updateBook(bookList);
            xinBiQuThread = new XinBiQuThread(document,bookUrlList.getUrl(), checkIdentify(bookList.getUrl()));

            xinBiQuThread.setName("新笔趣");
            xinBiQuThread.start();
            xinBiQuThread.join();

            resultUtil.setMsg("更新成功");
            resultUtil.setCode(200);
            resultUtil.setData(true);
            return resultUtil;
        } catch (Exception e) {
            e.printStackTrace();
            resultUtil.setCode(500);
            resultUtil.setData(false);
            resultUtil.setMsg("添加失败");
            return resultUtil;
        }
    }

    /**
     * 添加-全本小说
     * @param identify
     * @param urlId
     * @return
     */
    @Override
    public ResultUtil addQuanBenBook(String identify, String urlId) {
        ResultUtil resultUtil = new ResultUtil();
        BookList bookList =new BookList();
        BookUrlList bookUrlList = bookListDao.findUrlById(urlId);
        Document document = null;
        try {
            document = Jsoup.connect(bookUrlList.getUrl() + identify).get();
            // 书名
            String name = document.getElementsByTag("h2").get(0).text().replaceFirst("最新章节","");

            // 作者
            Elements info = document.select("div[class=small]");
            String author = info.get(0).child(0).text().split("：")[1];
            // 简介
            Elements intro = document.select("div[class=intro]");
            String introduction = intro.get(0).text();

            List<Element> list = document.getElementsByTag("dd");

            // 章节数量
            Integer count = list.size() - 9;
            if (StringUtils.isEmptyOrWhitespaceOnly(name)){
                resultUtil.setCode(500);
                resultUtil.setMsg("该网站没有此类书籍");
                return resultUtil;
            }

            bookList.setName(name);
            bookList.setParentId(urlId);
            bookList.setSize(count);
            bookList.setUrl(identify);
            bookList.setUpdateTime(new Date());
            bookList.setAuthor(author);
            bookList.setIntro(introduction);

//            Thread quanBenThread;
            if (checkIdentify(identify) == null) {
                bookList.setStatus(CommonUtil.STATUS_SAVE);
                bookList.setCreateTime(new Date());
                int i =bookListDao.insertBookList(bookList);
//                quanBenThread = new QuanBenThread(document,bookUrlList.getUrl(), bookList.getId());
            } else {
                bookListDao.updateBook(bookList);
//                quanBenThread = new QuanBenThread(document,bookUrlList.getUrl(), checkIdentify(identify));
            }
//            quanBenThread.setName("全本");
//            quanBenThread.start();

            resultUtil.setMsg("插入成功");
            resultUtil.setCode(200);
            resultUtil.setData(true);
            return resultUtil;
        } catch (IOException e) {
            e.printStackTrace();
            resultUtil.setCode(500);
            resultUtil.setData(false);
            resultUtil.setMsg("添加失败");
            return resultUtil;
        }
    }

    /**
     * 添加棉花糖小说
     * @param identify
     * @param urlId
     * @return
     */
    @Override
    public ResultUtil addMianHuaTang(String identify, String urlId) {
        ResultUtil resultUtil = new ResultUtil();
        BookList bookList =new BookList();
        BookUrlList bookUrlList = bookListDao.findUrlById(urlId);
        Document document = null;
        try {
            document = Jsoup.connect(bookUrlList.getUrl() + identify).get();
            // 书名
            String name = document.getElementsByTag("h1").get(0).text();

            // 作者
            Elements info = document.select("div[class=xiaoshuo]");
            String author = info.get(0).getElementsByTag("h6").select("a").text();

            // 简介
            String intro = document.select("p[id=intro]").text();

            // 列表
            List<Element> list = document.select("div[class=novel_list]").get(0).getElementsByTag("dl").get(0).select("dd");

            // 章节数量
            Integer size = list.size();

            // 下载地址
            String downloadHtml = document.select("div[class=book_cover]").get(0).getElementsByTag("a").get(0).attr("href");
            String url = Jsoup.connect(bookUrlList.getUrl() + downloadHtml).get().select("div[class=zip_down]").get(0).select("script").get(0).data();
            String downloadUrl= url.split("\"")[3];

            bookList.setName(name);
            bookList.setParentId(urlId);
            bookList.setSize(size);
            bookList.setUrl(identify);
            bookList.setUpdateTime(new Date());
            bookList.setAuthor(author);
            bookList.setIntro(intro);
            bookList.setDownloadUrl(downloadUrl);

            if (checkIdentify(identify) == null) {
                bookList.setStatus(CommonUtil.STATUS_SAVE);
                bookList.setCreateTime(new Date());
                int i =bookListDao.insertBookList(bookList);
            } else {
                bookListDao.updateBook(bookList);
            }

            resultUtil.setMsg("插入成功");
            resultUtil.setCode(200);
            resultUtil.setData(true);
            return resultUtil;
        } catch (IOException e) {
            e.printStackTrace();
            resultUtil.setCode(500);
            resultUtil.setData(false);
            resultUtil.setMsg("添加失败");
            return resultUtil;
        }
    }
}
